// src/app/features/paiement/paiement.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { ReservationsService } from '../../core/services/reservations.service';
import { PaiementsService } from '../../core/services/paiements.service';
import { Reservation } from '../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-paiement',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './paiement.component.html',
  styleUrls: ['./paiement.component.scss']
})
export class PaiementComponent implements OnInit {
  reservation: Reservation | null = null;
  paiementForm: FormGroup;
  isLoading = true;
  isProcessing = false;
  paymentStatus: 'idle' | 'pending' | 'success' | 'failed' = 'idle';
  transactionId: string = '';

  methodesDisponibles = [
    { value: 'MTN', label: 'MTN Mobile Money', icon: 'fas fa-mobile-alt', color: '#FFCC00' },
    { value: 'ORANGE', label: 'Orange Money', icon: 'fas fa-mobile-alt', color: '#FF6600' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private reservationsService: ReservationsService,
    private paiementsService: PaiementsService,
    private toastr: ToastrService
  ) {
    this.paiementForm = this.fb.group({
      methode: ['MTN', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^6[0-9]{8}$/)]]
    });
  }

  ngOnInit(): void {
    const reservationId = this.route.snapshot.params['id'];
    this.loadReservation(reservationId);
  }

  loadReservation(id: number): void {
    this.reservationsService.getReservation(id).subscribe({
      next: (reservation) => {
        this.reservation = reservation;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Réservation non trouvée', 'Erreur');
        this.router.navigate(['/mes-reservations']);
      }
    });
  }

  onSubmitPaiement(): void {
    if (this.paiementForm.invalid || !this.reservation) {
      this.toastr.warning('Veuillez remplir tous les champs', 'Formulaire incomplet');
      return;
    }

    this.isProcessing = true;
    this.paymentStatus = 'pending';

    const { methode, phoneNumber } = this.paiementForm.value;

    // Simulation de paiement selon la méthode
    const paiementRequest = {
      reservationId: this.reservation.id,
      phoneNumber,
      montant: this.reservation.prixTotal,
      methode
    };

    const initiatePayment = methode === 'MTN'
      ? this.paiementsService.initierPaiementMTN(paiementRequest)
      : this.paiementsService.initierPaiementOrange(paiementRequest);

    initiatePayment.subscribe({
      next: (response) => {
        this.transactionId = response.transactionId;
        this.toastr.info('Paiement en cours de traitement...', 'Veuillez patienter');

        // Simuler la vérification du statut après 3 secondes
        setTimeout(() => {
          this.verifierStatutPaiement();
        }, 3000);
      },
      error: () => {
        this.isProcessing = false;
        this.paymentStatus = 'failed';
        this.toastr.error('Erreur lors de l\'initiation du paiement', 'Échec');
      }
    });
  }

  verifierStatutPaiement(): void {
    this.paiementsService.verifierStatutPaiement(this.transactionId).subscribe({
      next: (response) => {
        if (response.statut === 'SUCCESS') {
          this.paymentStatus = 'success';
          this.isProcessing = false;
          this.toastr.success('Paiement effectué avec succès !', 'Succès');

          // Confirmer la réservation
          if (this.reservation) {
            this.reservationsService.confirmerReservation(this.reservation.id).subscribe();
          }
        } else {
          this.paymentStatus = 'failed';
          this.isProcessing = false;
          this.toastr.error('Le paiement a échoué', 'Échec');
        }
      },
      error: () => {
        this.paymentStatus = 'failed';
        this.isProcessing = false;
        this.toastr.error('Erreur lors de la vérification', 'Échec');
      }
    });
  }

  retryPayment(): void {
    this.paymentStatus = 'idle';
    this.isProcessing = false;
    this.paiementForm.reset({ methode: 'MTN' });
  }

  goToReservations(): void {
    this.router.navigate(['/mes-reservations']);
  }

  downloadReceipt(): void {
    this.toastr.info('Téléchargement du reçu...', 'PDF');
    // Implémenter la génération PDF ici (Issue 9)
  }

  getMethodeColor(): string {
    const methode = this.paiementForm.get('methode')?.value;
    return this.methodesDisponibles.find(m => m.value === methode)?.color || '#333';
  }
}
