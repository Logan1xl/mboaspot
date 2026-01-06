// src/app/core/auth/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'mboaspot_token';
  private readonly REFRESH_TOKEN_KEY = 'mboaspot_refresh_token';
  private readonly USER_KEY = 'mboaspot_user';

  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  /**
   * Connexion utilisateur
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => this.handleAuthSuccess(response))
      );
  }

  /**
   * Inscription utilisateur
   */
  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, userData)
      .pipe(
        tap(response => this.handleAuthSuccess(response))
      );
  }

  /**
   * Récupérer l'utilisateur connecté
   */
  getCurrentUser(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${environment.apiUrl}/auth/me`)
      .pipe(
        tap(response => {
          const user: User = {
            id: response.userId,
            email: response.email,
            nom: response.nom,
            prenom: response.prenom,
            role: response.role,
            numeroTelephone: response.numeroTelephone,
            photoProfil: response.photoProfil,
            estActif: true
          };
          this.saveUser(user);
        })
      );
  }

  /**
   * Rafraîchir le token
   */
  refreshToken(): Observable<{token: string}> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<{token: string}>(`${environment.apiUrl}/auth/refresh-token`, { refreshToken })
      .pipe(
        tap(response => {
          this.saveToken(response.token);
        })
      );
  }

  /**
   * Déconnexion
   */
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  /**
   * Vérifier si l'utilisateur est connecté
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired(token);
  }

  /**
   * Obtenir le rôle de l'utilisateur
   */
  getUserRole(): string | null {
    const user = this.currentUserSubject.value;
    return user ? user.role : null;
  }

  /**
   * Vérifier si l'utilisateur a un rôle spécifique
   */
  hasRole(role: string | string[]): boolean {
    const userRole = this.getUserRole();
    if (!userRole) return false;

    if (Array.isArray(role)) {
      return role.includes(userRole);
    }
    return userRole === role;
  }

  /**
   * Obtenir le token JWT
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Obtenir le refresh token
   */
  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  /**
   * Obtenir l'ID de l'utilisateur connecté
   */
  getCurrentUserId(): number | null {
    const user = this.currentUserSubject.value;
    return user ? user.id : null;
  }

  // ========== Méthodes privées ==========

  private handleAuthSuccess(response: AuthResponse): void {
    this.saveToken(response.token);
    if (response.refreshToken) {
      this.saveRefreshToken(response.refreshToken);
    }

    const user: User = {
      id: response.userId,
      email: response.email,
      nom: response.nom,
      prenom: response.prenom,
      role: response.role,
      numeroTelephone: response.numeroTelephone,
      photoProfil: response.photoProfil,
      estActif: true
    };

    this.saveUser(user);
  }

  private saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private saveRefreshToken(token: string): void {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  private saveUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getUserFromStorage(): User | null {
    const userJson = localStorage.getItem(this.USER_KEY);
    if (userJson) {
      try {
        return JSON.parse(userJson);
      } catch {
        return null;
      }
    }
    return null;
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp;
      return (Math.floor((new Date).getTime() / 1000)) >= expiry;
    } catch {
      return true;
    }
  }
}
