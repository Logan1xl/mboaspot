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
   * Note: Le token est généralement ajouté automatiquement par le JWT interceptor
   */
  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
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
   * Note: Les notifications sont gérées par l'interceptor HttpErrorInterceptor
   * pour éviter les doublons. Cette méthode propage simplement l'erreur.
   */
  private handleError(error: any) {
    // L'interceptor HttpErrorInterceptor gère déjà les notifications d'erreur
    // On propage simplement l'erreur pour que les composants puissent la gérer si nécessaire
    return throwError(() => error);
  }
}

