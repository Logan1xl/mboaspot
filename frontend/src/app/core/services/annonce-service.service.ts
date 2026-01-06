import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import {environment} from '../../../environments/environment';

export interface AnnonceDTO {
  id?: number;
  titre: string;
  prix: number;
  adresse: string;
  latitude?: number;
  longitude?: number;
  ville: string;
  quartier?: string;
  nbreChambres: number;
  nbreLits?: number;
  maxInvites: number;
  description: string;
  typeAnnonce: string;
  estActive?: boolean;
  evaluationMoyenne?: number;
  totalAvis?: number;
  urlImagePrincipale?: string;
  urlImages?: string[];
  idProprietaire?: number;
  proprietaireNom?: string;
  proprietaireEntreprise?: string;
  distance?: number;
}

export interface RechercheDTO {
  ville?: string;
  quartier?: string;
  typeAnnonce?: string;
  prixMin?: number;
  prixMax?: number;
  nbreChambresMin?: number;
  nbreLitsMin?: number;
  maxInvitesMin?: number;
  evaluationMin?: number;
  latitude?: number;
  longitude?: number;
  rayon?: number;
  surfaceMin?: number;
}

export interface Statistiques {
  totalAnnonces: number;
  villesDisponibles: number;
  typesLogements: number;
}

@Injectable({
  providedIn: 'root'
})
export class AnnonceService {
  private apiUrl = `${environment.apiUrl}/annonces`;

  constructor(private http: HttpClient) {}

  /**
   * Récupère toutes les annonces actives
   */
  getAnnoncesActives(): Observable<AnnonceDTO[]> {
    return this.http.get<AnnonceDTO[]>(`${this.apiUrl}/actives`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère une annonce par ID
   */
  getAnnonceById(id: number): Observable<AnnonceDTO> {
    return this.http.get<AnnonceDTO>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Recherche avancée d'annonces
   */
  rechercherAnnonces(recherche: RechercheDTO): Observable<AnnonceDTO[]> {
    return this.http.post<AnnonceDTO[]>(`${this.apiUrl}/recherche`, recherche).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Recherche rapide avec paramètres query string
   */
  rechercheRapide(params: {
    ville?: string;
    type?: string;
    prixMin?: number;
    prixMax?: number;
    chambres?: number;
    invites?: number;
  }): Observable<AnnonceDTO[]> {
    let httpParams = new HttpParams();

    if (params.ville) httpParams = httpParams.set('ville', params.ville);
    if (params.type) httpParams = httpParams.set('type', params.type);
    if (params.prixMin) httpParams = httpParams.set('prixMin', params.prixMin.toString());
    if (params.prixMax) httpParams = httpParams.set('prixMax', params.prixMax.toString());
    if (params.chambres) httpParams = httpParams.set('chambres', params.chambres.toString());
    if (params.invites) httpParams = httpParams.set('invites', params.invites.toString());

    return this.http.get<AnnonceDTO[]>(`${this.apiUrl}/recherche-rapide`, { params: httpParams }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les villes disponibles
   */
  getVillesDisponibles(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/villes`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les quartiers d'une ville
   */
  getQuartiersByVille(ville: string): Observable<string[]> {
    const params = new HttpParams().set('ville', ville);
    return this.http.get<string[]>(`${this.apiUrl}/quartiers`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les types d'annonces disponibles
   */
  getTypesAnnonces(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/types`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les statistiques
   */
  getStatistiques(): Observable<Statistiques> {
    return this.http.get<Statistiques>(`${this.apiUrl}/statistiques`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les annonces recommandées
   */
  getAnnoncesRecommandees(limit: number = 6): Observable<AnnonceDTO[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<AnnonceDTO[]>(`${this.apiUrl}/recommandations`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère le top des annonces
   */
  getTopAnnonces(): Observable<AnnonceDTO[]> {
    return this.http.get<AnnonceDTO[]>(`${this.apiUrl}/top`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Récupère les fourchettes de prix
   */
  getFourchettePrix(): Observable<{ min: number; max: number; moyenne: number }> {
    return this.http.get<{ min: number; max: number; moyenne: number }>(`${this.apiUrl}/fourchettes-prix`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Crée une nouvelle annonce
   */
  createAnnonce(annonce: AnnonceDTO): Observable<AnnonceDTO> {
    return this.http.post<AnnonceDTO>(this.apiUrl, annonce).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Met à jour une annonce
   */
  updateAnnonce(id: number, annonce: AnnonceDTO): Observable<AnnonceDTO> {
    return this.http.put<AnnonceDTO>(`${this.apiUrl}/${id}`, annonce).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Supprime une annonce
   */
  deleteAnnonce(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Active ou désactive une annonce
   */
  activerAnnonce(id: number, activer: boolean): Observable<void> {
    const params = new HttpParams().set('activer', activer.toString());
    return this.http.patch<void>(`${this.apiUrl}/${id}/activer`, null, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Gestion des erreurs
   */
  private handleError(error: any): Observable<never> {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      errorMessage = `Code erreur: ${error.status}\nMessage: ${error.message}`;

      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      }
    }

    console.error('Erreur API:', errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
