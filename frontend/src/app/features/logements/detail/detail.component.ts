// src/app/features/logements/detail/detail.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { MapComponent } from '../../../shared/components/map/map.component';
import { AnnoncesService } from '../../../core/services/annonces.service';
import { ReservationsService } from '../../../core/services/reservations.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Annonce } from '../../../core/models';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NavbarComponent,
    MapComponent
  ],
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  annonce: Annonce | null = null;
  isLoading = true;
  currentImageIndex = 0;
  showReservationModal = false;
  reservationForm: FormGroup;
  isCheckingAvailability = false;
  isCreatingReservation = false;
  today: string;

  apiUrl = environment.apiUrl;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private annoncesService: AnnoncesService,
    private reservationsService: ReservationsService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.today = new Date().toISOString().split('T')[0];
    this.reservationForm = this.fb.group({
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      nombreInvites: [1, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.loadAnnonce(id);
  }

  loadAnnonce(id: number): void {
    this.annoncesService.getAnnonceById(id).subscribe({
      next: (annonce) => {
        this.annonce = annonce;
        this.isLoading = false;

        // Mettre à jour le max invités
        if (annonce.maxInvites) {
          this.reservationForm.get('nombreInvites')?.setValidators([
            Validators.required,
            Validators.min(1),
            Validators.max(annonce.maxInvites)
          ]);
        }
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Logement non trouvé', 'Erreur');
        this.router.navigate(['/logements']);
      }
    });
  }

  getImages(): string[] {
    if (!this.annonce) return [];

    const images: string[] = [];

    if (this.annonce.urlImagePrincipale) {
      images.push(`${this.apiUrl}${this.annonce.urlImagePrincipale}`);
    }

    if (this.annonce.urlImages && this.annonce.urlImages.length > 0) {
      this.annonce.urlImages.forEach(url => {
        images.push(`${this.apiUrl}${url}`);
      });
    }

    return images.length > 0 ? images : ['assets/images/default-house.jpg'];
  }

  nextImage(): void {
    const images = this.getImages();
    this.currentImageIndex = (this.currentImageIndex + 1) % images.length;
  }

  prevImage(): void {
    const images = this.getImages();
    this.currentImageIndex = this.currentImageIndex === 0 ? images.length - 1 : this.currentImageIndex - 1;
  }

  selectImage(index: number): void {
    this.currentImageIndex = index;
  }

  getStars(): number[] {
    return Array(5).fill(0).map((_, i) => i);
  }

  isStarFilled(index: number): boolean {
    return index < Math.floor(this.annonce?.evaluationMoyenne || 0);
  }

  openReservationModal(): void {
    if (!this.authService.isAuthenticated()) {
      this.toastr.warning('Veuillez vous connecter pour réserver', 'Connexion requise');
      this.router.navigate(['/auth/login'], {
        queryParams: { returnUrl: this.router.url }
      });
      return;
    }

    if (!this.authService.hasRole('VOYAGEUR')) {
      this.toastr.error('Seuls les voyageurs peuvent réserver', 'Accès refusé');
      return;
    }

    this.showReservationModal = true;
  }

  closeReservationModal(): void {
    this.showReservationModal = false;
    this.reservationForm.reset({ nombreInvites: 1 });
  }

  verifierDisponibilite(): void {
    if (this.reservationForm.invalid || !this.annonce) {
      this.toastr.warning('Veuillez remplir tous les champs', 'Formulaire incomplet');
      return;
    }

    this.isCheckingAvailability = true;
    const formValue = this.reservationForm.value;

    this.reservationsService.verifierDisponibilite({
      annonceId: this.annonce.id,
      dateDebut: new Date(formValue.dateDebut),
      dateFin: new Date(formValue.dateFin),
      nombreInvites: formValue.nombreInvites
    }).subscribe({
      next: (disponible) => {
        this.isCheckingAvailability = false;
        if (disponible) {
          this.toastr.success('Le logement est disponible !', 'Disponible');
        } else {
          this.toastr.error('Le logement n\'est pas disponible pour ces dates', 'Indisponible');
        }
      },
      error: () => {
        this.isCheckingAvailability = false;
        this.toastr.error('Erreur lors de la vérification', 'Erreur');
      }
    });
  }

  creerReservation(): void {
    if (this.reservationForm.invalid || !this.annonce) {
      this.toastr.warning('Veuillez remplir tous les champs', 'Formulaire incomplet');
      return;
    }

    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      this.toastr.error('Utilisateur non connecté', 'Erreur');
      return;
    }

    this.isCreatingReservation = true;
    const formValue = this.reservationForm.value;

    this.reservationsService.creerReservation({
      annonceId: this.annonce.id,
      voyageurId: userId,
      dateDebut: new Date(formValue.dateDebut),
      dateFin: new Date(formValue.dateFin),
      nombreInvites: formValue.nombreInvites
    }).subscribe({
      next: (reservation) => {
        this.isCreatingReservation = false;
        this.toastr.success('Réservation créée avec succès !', 'Succès');
        this.closeReservationModal();

        // Rediriger vers la page de paiement
        this.router.navigate(['/paiement', reservation.id]);
      },
      error: (error) => {
        this.isCreatingReservation = false;
        const message = error.error?.message || 'Erreur lors de la réservation';
        this.toastr.error(message, 'Erreur');
      }
    });
  }

  calculerPrixTotal(): number {
    if (!this.annonce || !this.reservationForm.value.dateDebut || !this.reservationForm.value.dateFin) {
      return 0;
    }

    const debut = new Date(this.reservationForm.value.dateDebut);
    const fin = new Date(this.reservationForm.value.dateFin);
    const jours = Math.ceil((fin.getTime() - debut.getTime()) / (1000 * 60 * 60 * 24));

    return jours > 0 ? jours * this.annonce.prix : 0;
  }
  // test git
}
