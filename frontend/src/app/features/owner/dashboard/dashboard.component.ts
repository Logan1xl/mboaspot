// src/app/features/owner/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { AnnoncesService } from '../../../core/services/annonces.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Annonce, DashboardStats } from '../../../core/models';

@Component({
  selector: 'app-owner-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats = {
    totalAnnonces: 0,
    totalReservations: 0,
    revenuTotal: 0
  };
  annoncesRecentes: Annonce[] = [];
  isLoading = true;

  constructor(
    private annoncesService: AnnoncesService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      this.isLoading = false;
      return;
    }

    // Charger les annonces du propriétaire
    this.annoncesService.getAnnoncesProprietaire(userId).subscribe({
      next: (annonces) => {
        this.annoncesRecentes = annonces.slice(0, 5);
        this.stats.totalAnnonces = annonces.length;

        // Calculer les stats
        this.stats.totalReservations = 0; // À implémenter avec API
        this.stats.revenuTotal = 0; // À implémenter avec API

        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  getStatusClass(isActive: boolean): string {
    return isActive ? 'status-active' : 'status-inactive';
  }

  getStatusLabel(isActive: boolean): string {
    return isActive ? 'Active' : 'Inactive';
  }
}
