// src/app/features/reservations/mes-reservations/mes-reservations.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { ReservationsService } from '../../../core/services/reservations.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Reservation } from '../../../core/models';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-mes-reservations',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.scss']
})
export class MesReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  filteredReservations: Reservation[] = [];
  isLoading = true;
  activeFilter: 'all' | 'EN_ATTENTE' | 'CONFIRMEE' | 'ANNULEE' | 'TERMINEE' = 'all';

  filters = [
    { value: 'all', label: 'Toutes', count: 0 },
    { value: 'EN_ATTENTE', label: 'En attente', count: 0 },
    { value: 'CONFIRMEE', label: 'Confirmées', count: 0 },
    { value: 'TERMINEE', label: 'Terminées', count: 0 },
    { value: 'ANNULEE', label: 'Annulées', count: 0 }
  ];

  constructor(
    private reservationsService: ReservationsService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      this.isLoading = false;
      return;
    }

    this.reservationsService.getReservationsVoyageur(userId).subscribe({
      next: (reservations) => {
        this.reservations = reservations;
        this.updateFilterCounts();
        this.applyFilter('all');
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.toastr.error('Erreur lors du chargement', 'Erreur');
      }
    });
  }

  updateFilterCounts(): void {
    this.filters.forEach(filter => {
      if (filter.value === 'all') {
        filter.count = this.reservations.length;
      } else {
        filter.count = this.reservations.filter(r => r.statut === filter.value).length;
      }
    });
  }

  applyFilter(filterValue: string): void {
    this.activeFilter = filterValue as any;

    if (filterValue === 'all') {
      this.filteredReservations = [...this.reservations];
    } else {
      this.filteredReservations = this.reservations.filter(r => r.statut === filterValue);
    }
  }

  annulerReservation(reservation: Reservation): void {
    if (!confirm('Êtes-vous sûr de vouloir annuler cette réservation ?')) {
      return;
    }

    this.reservationsService.annulerReservation(reservation.id).subscribe({
      next: () => {
        this.toastr.success('Réservation annulée', 'Succès');
        this.loadReservations();
      },
      error: (error) => {
        const message = error.error?.message || 'Erreur lors de l\'annulation';
        this.toastr.error(message, 'Erreur');
      }
    });
  }

  getStatusClass(statut: string): string {
    const classes: Record<string, string> = {
      'EN_ATTENTE': 'status-pending',
      'CONFIRMEE': 'status-confirmed',
      'TERMINEE': 'status-completed',
      'ANNULEE': 'status-cancelled'
    };
    return classes[statut] || 'status-default';
  }

  getStatusLabel(statut: string): string {
    const labels: Record<string, string> = {
      'EN_ATTENTE': 'En attente',
      'CONFIRMEE': 'Confirmée',
      'TERMINEE': 'Terminée',
      'ANNULEE': 'Annulée'
    };
    return labels[statut] || statut;
  }

  getStatusIcon(statut: string): string {
    const icons: Record<string, string> = {
      'EN_ATTENTE': 'fas fa-clock',
      'CONFIRMEE': 'fas fa-check-circle',
      'TERMINEE': 'fas fa-flag-checkered',
      'ANNULEE': 'fas fa-times-circle'
    };
    return icons[statut] || 'fas fa-info-circle';
  }

  canCancel(reservation: Reservation): boolean {
    return reservation.statut === 'EN_ATTENTE' || reservation.statut === 'CONFIRMEE';
  }

  canPay(reservation: Reservation): boolean {
    return reservation.statut === 'EN_ATTENTE';
  }
}
