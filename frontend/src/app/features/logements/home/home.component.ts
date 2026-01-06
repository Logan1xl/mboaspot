// src/app/features/logements/home/home.component.ts
import { Component, OnInit, ViewChild } from '@angular/core';
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
    RouterLink,
    ReactiveFormsModule,
    NavbarComponent,
    CardLogementComponent,
    MapComponent
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  searchForm: FormGroup;
  topAnnonces: Annonce[] = [];
  villes: string[] = [];
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private annoncesService: AnnoncesService,
    private router: Router
  ) {
    this.searchForm = this.fb.group({
      ville: [''],
      dateDebut: [''],
      dateFin: [''],
      nombrePersonnes: [1]
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
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadVilles(): void {
    this.annoncesService.getVillesDisponibles().subscribe({
      next: (villes) => {
        this.villes = villes;
      }
    });
  }

  onSearch(): void {
    const filters = this.searchForm.value;
    this.router.navigate(['/logements'], { queryParams: filters });
  }

  onFavoriteToggle(annonceId: number): void {
    console.log('Toggle favorite:', annonceId);
    // Implémenter la logique des favoris
  }
}
