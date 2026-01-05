// src/app/core/services/annonces.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Annonce,
  RechercheFilters,
  Localisation,
  Disponibilite,
  Equipement,
} from '../models';

@Injectable({
  providedIn: 'root',
})
export class AnnoncesService {
  private apiUrl = `${environment.apiUrl}/annonces`;

  constructor(private http: HttpClient) {}

  // ========== CRUD Annonces ==========

  getAllAnnonces(): Observable<Annonce[]> {
    return this.http.get<Annonce[]>(this.apiUrl);
  }

  getAnnonceById(id: number): Observable<Annonce> {
    return this.http.get<Annonce>(`${this.apiUrl}/${id}`);
  }

  getAnnoncesActives(): Observable<Annonce[]> {
    return this.http.get<Annonce[]>(`${this.apiUrl}/actives`);
  }

  createAnnonce(annonce: Partial<Annonce>): Observable<Annonce> {
    return this.http.post<Annonce>(this.apiUrl, annonce);
  }

  updateAnnonce(id: number, annonce: Partial<Annonce>): Observable<Annonce> {
    return this.http.put<Annonce>(`${this.apiUrl}/${id}`, annonce);
  }

  deleteAnnonce(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activerAnnonce(id: number, activer: boolean): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/activer`, null, {
      params: { activer: activer.toString() },
    });
  }

  // ========== Recherche ==========

  rechercherAnnonces(filters: RechercheFilters): Observable<Annonce[]> {
    return this.http.post<Annonce[]>(`${this.apiUrl}/recherche`, filters);
  }

  getTopAnnonces(): Observable<Annonce[]> {
    return this.http.get<Annonce[]>(`${this.apiUrl}/top`);
  }

  getVillesDisponibles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/villes`);
  }

  getQuartiersByVille(ville: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/quartiers`, {
      params: { ville },
    });
  }

  getTypes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/types`);
  }

  // ========== Disponibilités ==========

  getDisponibilites(annonceId: number): Observable<Disponibilite[]> {
    return this.http.get<Disponibilite[]>(
      `${this.apiUrl}/${annonceId}/disponibilites`
    );
  }

  addDisponibilite(disponibilite: Disponibilite): Observable<Disponibilite> {
    return this.http.post<Disponibilite>(
      `${this.apiUrl}/disponibilites`,
      disponibilite
    );
  }

  verifierDisponibilite(
    annonceId: number,
    dateDebut: Date,
    dateFin: Date
  ): Observable<boolean> {
    const params = new HttpParams()
      .set('dateDebut', dateDebut.toISOString())
      .set('dateFin', dateFin.toISOString());

    return this.http.get<boolean>(
      `${this.apiUrl}/${annonceId}/verifier-disponibilite`,
      { params }
    );
  }

  // ========== Localisation ==========

  getLocalisation(annonceId: number): Observable<Localisation> {
    return this.http.get<Localisation>(
      `${this.apiUrl}/${annonceId}/localisation`
    );
  }

  updateLocalisation(
    annonceId: number,
    localisation: Localisation
  ): Observable<Localisation> {
    return this.http.put<Localisation>(
      `${this.apiUrl}/${annonceId}/localisation`,
      localisation
    );
  }

  // ========== Propriétaire ==========

  getAnnoncesProprietaire(proprietaireId: number): Observable<Annonce[]> {
    return this.http.get<Annonce[]>(
      `${this.apiUrl}/v1/proprietaire/${proprietaireId}`
    );
  }
}
