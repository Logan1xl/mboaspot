import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardLogementComponent } from '../../../shared/components/card-logement/card-logement.component';
import { ApiService } from '../../../core/services/api.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-listing',
  standalone: true,
  imports: [CommonModule, FormsModule, CardLogementComponent],
  template: `
    <div class="listing-page">
      <div class="controls">
        <div class="left">
          <label>Tri:</label>
          <select [(ngModel)]="sortType" (change)="applySort()">
            <option value="">Par défaut</option>
            <option value="priceAsc">Prix ↑</option>
            <option value="priceDesc">Prix ↓</option>
            <option value="ratingDesc">Notes ↓</option>
            <option value="newest">Nouvelles annonces</option>
          </select>
        </div>
        <div class="right">
          <span *ngIf="loading">Chargement...</span>
          <span *ngIf="error" class="error">{{ error }}</span>
        </div>
      </div>

      <div class="grid" *ngIf="!loading && !error">
        <ng-container *ngFor="let item of pagedData">
          <app-card-logement [logement]="item" (favorite)="onFavorite($event)"></app-card-logement>
        </ng-container>
      </div>

      <div class="empty" *ngIf="!loading && !pagedData?.length">Aucun logement trouvé.</div>

      <div class="pagination" *ngIf="totalPages > 1">
        <button (click)="changePage(currentPage-1)" [disabled]="currentPage===1">Préc</button>
        <button *ngFor="let p of pages" (click)="changePage(p)" [class.active]="p===currentPage">{{p}}</button>
        <button (click)="changePage(currentPage+1)" [disabled]="currentPage===totalPages">Suiv</button>
      </div>
    </div>
  `,
  styles: [`
    .listing-page {
      padding: 24px 16px;
      max-width: 1400px;
      margin: 0 auto;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      min-height: 100vh;
    }

    .controls {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 32px;
      background: white;
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      flex-wrap: wrap;
      gap: 16px;
    }

    .left, .right {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .controls label {
      font-weight: 600;
      color: #2c3e50;
    }

    .controls select {
      padding: 10px 12px;
      border: 2px solid #e0e0e0;
      border-radius: 8px;
      font-size: 14px;
      cursor: pointer;
      transition: all 200ms ease;
    }

    .controls select:hover {
      border-color: #2ecc71;
    }

    .controls select:focus {
      outline: none;
      border-color: #2ecc71;
      box-shadow: 0 0 0 3px rgba(46, 204, 113, 0.1);
    }

    .grid {
      display: grid;
      gap: 24px;
      grid-template-columns: repeat(1, 1fr);
      margin-bottom: 32px;
    }

    @media (min-width: 768px) {
      .grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (min-width: 1200px) {
      .grid {
        grid-template-columns: repeat(3, 1fr);
      }
    }

    .empty {
      grid-column: 1 / -1;
      text-align: center;
      padding: 60px 20px;
      color: #666;
      font-size: 18px;
      background: white;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }

    .pagination {
      display: flex;
      gap: 8px;
      justify-content: center;
      flex-wrap: wrap;
      margin-top: 32px;
      padding: 20px;
      background: white;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }

    .pagination button {
      padding: 10px 14px;
      border: 2px solid #e0e0e0;
      background: white;
      color: #2c3e50;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 600;
      transition: all 200ms ease;
      min-width: 44px;
    }

    .pagination button:hover:not(:disabled) {
      background: #2ecc71;
      border-color: #2ecc71;
      color: white;
    }

    .pagination button.active {
      background: #2ecc71;
      color: white;
      border-color: #2ecc71;
    }

    .pagination button:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .error {
      color: #e07a5f;
      font-weight: 600;
      background: #fff5f0;
      padding: 12px 16px;
      border-radius: 8px;
    }
  `]
})
export class ListingComponent implements OnInit {
  // Données brutes récupérées depuis l'API
  data: any[] = [];
  // Données affichées pour la page courante
  pagedData: any[] = [];
  // Etats d'UI
  loading = false;
  error: string | null = null;

  // Pagination
  currentPage = 1;
  pageSize = 10; // Critère d'acceptation: 10 items par page
  totalPages = 0;
  pages: number[] = [];

  // Tri
  sortType = '';

  constructor(private api: ApiService) {}

  /** Au démarrage du composant on récupère les annonces */
  ngOnInit(): void {
    this.fetch();
  }

  /**
   * fetch: appelle l'endpoint `/api/annonces` et rempli `data`.
   * Gère l'état `loading` et les erreurs pour l'affichage.
   */
  fetch() {
    this.loading = true;
    this.error = null;
    this.api.get<any[]>('/annonces').subscribe({
      next: (res) => {
        // Assurer un tableau même si l'API renvoie null
        this.data = res || [];
        // Appliquer le tri courant (si présent)
        this.applySort(false);
        // Préparer la pagination
        this.setupPagination();
        this.loading = false;
      },
      error: (err) => {
        // L'ApiService convertit et affiche déjà une notification; ici on conserve un message lisible
        this.error = err?.message || 'Erreur lors de la récupération';
        this.loading = false;
      }
    });
  }

  /**
   * Tri côté client. `resetPage` permet de revenir à la 1ère page quand on change de tri.
   */
  applySort(resetPage = true) {
    if (!this.data) return;
    let arr = [...this.data];
    switch(this.sortType) {
      case 'priceAsc': arr.sort((a,b)=> (a.prix||0)-(b.prix||0)); break;
      case 'priceDesc': arr.sort((a,b)=> (b.prix||0)-(a.prix||0)); break;
      case 'ratingDesc': arr.sort((a,b)=> (b.noteMoyenne||b.rating||0)-(a.noteMoyenne||a.rating||0)); break;
      case 'newest': arr.sort((a,b)=> new Date(b.createdAt || b.date || 0).getTime() - new Date(a.createdAt || a.date || 0).getTime()); break;
      default: break;
    }
    this.data = arr;
    if (resetPage) { this.currentPage = 1; }
    this.updatePagedData();
  }

  /** Initialise la pagination en fonction de la longueur des données */
  setupPagination() {
    this.totalPages = Math.max(1, Math.ceil((this.data?.length||0)/this.pageSize));
    this.pages = Array.from({length: this.totalPages}, (_,i)=>i+1);
    this.currentPage = Math.min(this.currentPage, this.totalPages);
    this.updatePagedData();
  }

  /** Calcule `pagedData` en découpant `data` pour la page courante */
  updatePagedData() {
    const start = (this.currentPage-1)*this.pageSize;
    this.pagedData = this.data.slice(start, start + this.pageSize);
    this.totalPages = Math.max(1, Math.ceil((this.data?.length||0)/this.pageSize));
    this.pages = Array.from({length: this.totalPages}, (_,i)=>i+1);
  }

  /** Change la page affichée */
  changePage(p: number) {
    if (p < 1 || p > this.totalPages) return;
    this.currentPage = p;
    this.updatePagedData();
  }

  /**
   * onFavorite: appelé par `CardLogementComponent` lorsqu'on clique sur Favoris.
   * Envoie une requête POST vers `/api/favori` avec l'id de l'annonce.
   * L'`ApiService` gère l'affichage des notifications et des erreurs.
   */
  onFavorite(item: any) {
    if (!item?.id && !item?.annonceId) {
      console.warn('Cannot favorite item without id', item);
      return;
    }
    const payload = { annonceId: item.id ?? item.annonceId };
    this.api.post('/favori', payload).subscribe({
      next: () => { /* Succès géré par ApiService */ },
      error: () => { /* Erreurs déjà gérées par ApiService */ }
    });
  }
}
