import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CardLogementComponent } from '../../../shared/components/card-logement/card-logement.component';
import { AnnoncesService } from '../../../core/services/annonces.service';
import { Annonce } from '../../../core/models';
import { MapComponent } from '../../../shared/components/map/map.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NavbarComponent,
    CardLogementComponent,
    MapComponent,
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  searchForm: FormGroup;
  topAnnonces: Annonce[] = [];
  villes: string[] = [];
  isLoading = false;
  today: string = new Date().toISOString().split('T')[0];

  /** Marqueurs pour la carte */
  mapMarkers: {
    lat: number;
    lng: number;
    title?: string;
    data?: any;
  }[] = [];

  constructor(
    private fb: FormBuilder,
    private annoncesService: AnnoncesService,
    private router: Router
  ) {
    this.searchForm = this.fb.group({
      ville: [''],
      dateDebut: [''],
      dateFin: [''],
      nombrePersonnes: [1],
    });
  }

  ngOnInit(): void {
    this.loadTopAnnonces();
    this.loadVilles();
  }

  loadTopAnnonces(): void {
    this.isLoading = true;

    this.annoncesService.getTopAnnonces().subscribe({
      next: (annonces) => {
        this.topAnnonces = annonces.slice(0, 6);
        this.buildMapMarkers(this.topAnnonces);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadVilles(): void {
    this.annoncesService.getVillesDisponibles().subscribe({
      next: (villes) => {
        this.villes = villes;
      },
    });
  }

  onSearch(): void {
    this.router.navigate(['/logements'], {
      queryParams: this.searchForm.value,
    });
  }

  onFavoriteToggle(annonceId: number): void {
    // TODO : implémenter favoris
  }

  /** Conversion annonces → marqueurs carte */
  private buildMapMarkers(annonces: Annonce[]): void {
    this.mapMarkers = annonces
      .filter((a) => a.latitude && a.longitude)
      .map((a) => ({
        lat: a.latitude!,
        lng: a.longitude!,
        title: a.titre,
        data: a, // optionnel : utile si tu veux ouvrir un popup
      }));
  }
}
