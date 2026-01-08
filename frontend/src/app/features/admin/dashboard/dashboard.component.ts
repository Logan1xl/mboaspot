// src/app/features/admin/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

interface AdminStats {
  totalUsers: number;
  totalAnnonces: number;
  totalReservations: number;
  revenuTotal: number;
  tauxOccupation: number;
  villesPopulaires: Array<{ville: string, count: number}>;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  stats: AdminStats = {
    totalUsers: 0,
    totalAnnonces: 0,
    totalReservations: 0,
    revenuTotal: 0,
    tauxOccupation: 0,
    villesPopulaires: []
  };

  recentUsers: any[] = [];
  annoncesEnAttente: any[] = [];
  signalements: any[] = [];
  isLoading = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
    this.loadRecentUsers();
    this.loadAnnoncesEnAttente();
    this.loadSignalements();
  }

  loadStatistics(): void {
    // Simulation de stats (à remplacer par un vrai appel API)
    setTimeout(() => {
      this.stats = {
        totalUsers: 1250,
        totalAnnonces: 487,
        totalReservations: 2341,
        revenuTotal: 45780000,
        tauxOccupation: 72.5,
        villesPopulaires: [
          { ville: 'Douala', count: 245 },
          { ville: 'Yaoundé', count: 183 },
          { ville: 'Bafoussam', count: 59 }
        ]
      };
      this.isLoading = false;
    }, 1000);

    
    this.http.get<AdminStats>(`${environment.apiUrl}/admin/statistics`).subscribe({
      next: (stats) => {
        this.stats = stats;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });

  }

  loadRecentUsers(): void {
    // Simulation (à remplacer)
    this.recentUsers = [
      { id: 1, nom: 'Dupont', prenom: 'Jean', email: 'jean@example.com', role: 'VOYAGEUR', estActif: true },
      { id: 2, nom: 'Martin', prenom: 'Marie', email: 'marie@example.com', role: 'PROPRIETAIRE', estActif: true },
      { id: 3, nom: 'Bernard', prenom: 'Paul', email: 'paul@example.com', role: 'VOYAGEUR', estActif: false }
    ];
  }

  loadAnnoncesEnAttente(): void {
    // Simulation
    this.annoncesEnAttente = [
      { id: 1, titre: 'Appartement moderne Douala', proprietaire: 'Marie Martin', createdAt: new Date() },
      { id: 2, titre: 'Villa spacieuse Yaoundé', proprietaire: 'Pierre Dubois', createdAt: new Date() }
    ];
  }

  loadSignalements(): void {
    // Simulation
    this.signalements = [
      { id: 1, annonce: 'Studio Akwa', raison: 'Photos trompeuses', statut: 'EN_ATTENTE' },
      { id: 2, annonce: 'Chambre Bonanjo', raison: 'Prix abusif', statut: 'EN_ATTENTE' }
    ];
  }

  approuverAnnonce(annonce: any): void {
    if (confirm(`Approuver l'annonce "${annonce.titre}" ?`)) {
      // TODO: Appel API pour approuver l'annonce
      this.loadAnnoncesEnAttente();
    }
  }

  rejeterAnnonce(annonce: any): void {
    const motif = prompt('Motif de rejet :');
    if (motif) {
      // TODO: Appel API pour rejeter l'annonce avec le motif
      this.loadAnnoncesEnAttente();
    }
  }

  traiterSignalement(signalement: any): void {
    if (confirm(`Marquer ce signalement comme traité ?`)) {
      // TODO: Appel API pour traiter le signalement
      this.loadSignalements();
    }
  }

  toggleUserStatus(user: any): void {
    const action = user.estActif ? 'désactiver' : 'activer';
    if (confirm(`Voulez-vous ${action} cet utilisateur ?`)) {
      // TODO: Appel API pour changer le statut de l'utilisateur
      this.loadRecentUsers();
    }
  }

  getRoleLabel(role: string): string {
    const labels: Record<string, string> = {
      'VOYAGEUR': 'Voyageur',
      'PROPRIETAIRE': 'Propriétaire',
      'ADMIN': 'Administrateur'
    };
    return labels[role] || role;
  }

  getRoleBadgeClass(role: string): string {
    const classes: Record<string, string> = {
      'VOYAGEUR': 'badge-voyageur',
      'PROPRIETAIRE': 'badge-owner',
      'ADMIN': 'badge-admin'
    };
    return classes[role] || '';
  }
}
