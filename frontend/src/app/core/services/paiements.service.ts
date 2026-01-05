
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, delay } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Paiement, PaiementRequest } from '../models';

@Injectable({
  providedIn: 'root'
})
export class PaiementsService {
  private apiUrl = `${environment.apiUrl}/paiement`;

  constructor(private http: HttpClient) {}

  // Simulation paiement MTN
  initierPaiementMTN(request: PaiementRequest): Observable<{transactionId: string, statut: string}> {
    // Simuler l'appel API
    return of({
      transactionId: 'MTN-' + Date.now(),
      statut: 'PENDING'
    }).pipe(delay(1000)); // Simule un délai réseau
  }

  // Simulation paiement Orange
  initierPaiementOrange(request: PaiementRequest): Observable<{transactionId: string, statut: string}> {
    return of({
      transactionId: 'OM-' + Date.now(),
      statut: 'PENDING'
    }).pipe(delay(1000));
  }

  // Vérifier statut (simulation)
  verifierStatutPaiement(transactionId: string): Observable<{statut: string}> {
    // Simuler succès après 3 secondes
    return of({ statut: 'SUCCESS' }).pipe(delay(3000));
  }

  creerPaiement(paiement: Partial<Paiement>): Observable<Paiement> {
    return this.http.post<Paiement>(this.apiUrl, paiement);
  }

  getPaiement(id: number): Observable<Paiement> {
    return this.http.get<Paiement>(`${this.apiUrl}/${id}`);
  }

  getPaiementsReservation(reservationId: number): Observable<Paiement[]> {
    return this.http.get<Paiement[]>(`${this.apiUrl}/reservation/${reservationId}`);
  }

  mettreAJourStatut(id: number, statut: string): Observable<Paiement> {
    return this.http.patch<Paiement>(`${this.apiUrl}/${id}/statut`, { statut });
  }
}
