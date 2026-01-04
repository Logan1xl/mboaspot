import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth/auth.service';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private notificationService: NotificationService
  ) { }

  /**
   * Crée les headers HTTP avec le token d'authentification
   */
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  /**
   * GET - Récupère des données
   */
  get<T>(endpoint: string, params?: HttpParams): Observable<T> {
    return this.http.get<T>(
      `${this.apiUrl}${endpoint}`,
      { headers: this.getHeaders(), params }
    ).pipe(
      retry(1),
      catchError(error => this.handleError(error))
    );
  }

  /**
   * POST - Crée de nouvelles données
   */
  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(
      `${this.apiUrl}${endpoint}`,
      data,
      { headers: this.getHeaders() }
    ).pipe(
      catchError(error => this.handleError(error))
    );
  }

  /**
   * PUT - Met à jour des données existantes
   */
  put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(
      `${this.apiUrl}${endpoint}`,
      data,
      { headers: this.getHeaders() }
    ).pipe(
      catchError(error => this.handleError(error))
    );
  }

  /**
   * PATCH - Met à jour partiellement des données
   */
  patch<T>(endpoint: string, data: any): Observable<T> {
    return this.http.patch<T>(
      `${this.apiUrl}${endpoint}`,
      data,
      { headers: this.getHeaders() }
    ).pipe(
      catchError(error => this.handleError(error))
    );
  }

  /**
   * DELETE - Supprime des données
   */
  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(
      `${this.apiUrl}${endpoint}`,
      { headers: this.getHeaders() }
    ).pipe(
      catchError(error => this.handleError(error))
    );
  }

  /**
   * Gestion des erreurs HTTP
   */
  private handleError(error: any) {
    let errorMessage = 'Une erreur est survenue';

    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      if (error.status === 401) {
        // Non autorisé - rafraîchir le token
        this.authService.refreshToken().subscribe({
          error: () => this.authService.logout()
        });
        errorMessage = 'Session expirée, veuillez vous reconnecter';
      } else if (error.status === 403) {
        errorMessage = 'Accès refusé';
      } else if (error.status === 404) {
        errorMessage = 'Ressource non trouvée';
      } else if (error.status === 500) {
        errorMessage = 'Erreur serveur';
      } else {
        errorMessage = error.error?.message || 'Une erreur est survenue';
      }
    }

    this.notificationService.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}

