
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Reservation, ReservationRequest } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ReservationsService {
  private apiUrl = `${environment.apiUrl}/reservations`;

  constructor(private http: HttpClient) {}


  verifierDisponibilite(request: {
    annonceId: number;
    dateDebut: Date;
    dateFin: Date;
    nombreInvites: number;
  }): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/disponibilite`, request);
  }

  creerReservation(request: ReservationRequest): Observable<Reservation> {
    return this.http.post<Reservation>(this.apiUrl, request);
  }

  getReservation(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`);
  }

  getReservationsVoyageur(voyageurId: number): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/voyageur/${voyageurId}`);
  }

  getReservationByCode(code: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/confirmation/${code}`);
  }

  annulerReservation(id: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}/annuler`, {});
  }

  confirmerReservation(id: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.apiUrl}/${id}/confirmer`, {});
  }

  getToutesReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.apiUrl);
  }
}
