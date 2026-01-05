// src/app/features/logements/listing/listing.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CardLogementComponent } from '../../../shared/components/card-logement/card-logement.component';
import { AnnoncesService } from '../../../core/services/annonces.service';
import { Annonce, RechercheFilters } from '../../../core/models';

@Component({
  selector: 'app-listing',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NavbarComponent,
    CardLogementComponent
  ],
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.scss']
})
export class ListingComponent implements OnInit {
  annonces: Annonce[] = [];
  filteredAnnonces: Annonce[] = [];
  villes: string[] = [];
  types: string[] = [];
  filterForm: FormGroup;
  isLoading = false;
  showFilters = false;

  // Pagination
  currentPage = 0;
  pageSize = 9;
  totalPages = 0;

  // Tri
  sortOptions = [
    { value: 'price_asc', label: 'Prix croissant' },
    { value: 'price_desc', label: 'Prix décroissant' },
    { value: 'rating_desc', label: 'Mieux notés' },
    { value: 'recent', label: 'Plus récents' }
  ];

  constructor(
    private fb: FormBuilder,
    private annoncesService: AnnoncesService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.filterForm = this.fb.group({
      ville: [''],
      typeAnnonce: [''],
      prixMin: [null],
      prixMax: [null],
      nbreChambres: [null],
      evaluationMin: [null],
      sort: ['']
    });
  }

  ngOnInit(): void {
    this.loadVilles();
    this.loadTypes();

    // Récupérer les paramètres de recherche de l'URL
    this.route.queryParams.subscribe(params => {
      if (params['ville']) {
        this.filterForm.patchValue({
          ville: params['ville']
        });
      }
      this.applyFilters();
    });

    // Écouter les changements de filtres
    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  loadVilles(): void {
    this.annoncesService.getVillesDisponibles().subscribe({
      next: (villes) => {
        this.villes = villes;
      }
    });
  }

  loadTypes(): void {
    this.annoncesService.getTypes().subscribe({
      next: (types) => {
        this.types = types;
      }
    });
  }

  applyFilters(): void {
    this.isLoading = true;
    const filters: RechercheFilters = {
      ...this.filterForm.value,
      page: this.currentPage,
      size: this.pageSize
    };

    this.annoncesService.rechercherAnnonces(filters).subscribe({
      next: (annonces) => {
        this.annonces = annonces;
        this.sortAnnonces();
        this.paginateAnnonces();
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  sortAnnonces(): void {
    const sortValue = this.filterForm.get('sort')?.value;

    switch(sortValue) {
      case 'price_asc':
        this.annonces.sort((a, b) => a.prix - b.prix);
        break;
      case 'price_desc':
        this.annonces.sort((a, b) => b.prix - a.prix);
        break;
      case 'rating_desc':
        this.annonces.sort((a, b) => (b.evaluationMoyenne || 0) - (a.evaluationMoyenne || 0));
        break;
      case 'recent':
        this.annonces.sort((a, b) => b.id - a.id);
        break;
    }
  }

  paginateAnnonces(): void {
    this.totalPages = Math.ceil(this.annonces.length / this.pageSize);
    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    this.filteredAnnonces = this.annonces.slice(start, end);
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  resetFilters(): void {
    this.filterForm.reset();
    this.currentPage = 0;
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.paginateAnnonces();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  onFavoriteToggle(annonceId: number): void {
    console.log('Toggle favorite:', annonceId);
  }

  getPages(): number[] {
    return Array(this.totalPages).fill(0).map((_, i) => i);
  }
}
