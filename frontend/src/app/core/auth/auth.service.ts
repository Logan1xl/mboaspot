import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface User {
  id: string;
  email: string;
  name: string;
  role: 'client' | 'owner' | 'admin';
  avatar?: string;
  phone?: string;
  verified?: boolean;
}

export interface AuthToken {
  accessToken: string;
  refreshToken?: string;
  expiresIn?: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  private isLoggedInSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor() { }

  /**
   * Récupère le token du localStorage
   */
  private getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  /**
   * Vérifie s'il existe un token valide
   */
  private hasToken(): boolean {
    const token = this.getToken();
    return !!token;
  }

  /**
   * Récupère l'utilisateur du localStorage
   */
  private getUserFromStorage(): User | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }

  /**
   * Effectue la connexion
   */
  login(email: string, password: string): Observable<AuthToken> {
    // À remplacer par un appel API réel
    return new Observable(observer => {
      // Simulation API
      setTimeout(() => {
        const mockUser: User = {
          id: '1',
          email,
          name: 'User',
          role: 'client'
        };

        const mockToken: AuthToken = {
          accessToken: 'mock-token-' + Date.now()
        };

        this.setSession(mockUser, mockToken);
        observer.next(mockToken);
        observer.complete();
      }, 1000);
    });
  }

  /**
   * Effectue la déconnexion
   */
  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.isLoggedInSubject.next(false);
  }

  /**
   * Enregistre une nouvelle session
   */
  private setSession(user: User, authToken: AuthToken): void {
    localStorage.setItem('accessToken', authToken.accessToken);
    if (authToken.refreshToken) {
      localStorage.setItem('refreshToken', authToken.refreshToken);
    }
    localStorage.setItem('currentUser', JSON.stringify(user));
    this.currentUserSubject.next(user);
    this.isLoggedInSubject.next(true);
  }

  /**
   * Récupère l'utilisateur actuel
   */
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Vérifie si l'utilisateur est connecté
   */
  isLoggedIn(): boolean {
    return this.isLoggedInSubject.value;
  }

  /**
   * Rafraîchit le token
   */
  refreshToken(): Observable<AuthToken> {
    return new Observable(observer => {
      const refreshToken = localStorage.getItem('refreshToken');
      if (!refreshToken) {
        observer.error(new Error('No refresh token available'));
        return;
      }

      // À remplacer par un appel API réel
      setTimeout(() => {
        const mockToken: AuthToken = {
          accessToken: 'mock-token-' + Date.now()
        };
        localStorage.setItem('accessToken', mockToken.accessToken);
        observer.next(mockToken);
        observer.complete();
      }, 500);
    });
  }

  /**
   * Inscription d'un nouvel utilisateur
   */
  register(email: string, password: string, name: string): Observable<User> {
    return new Observable(observer => {
      // À remplacer par un appel API réel
      setTimeout(() => {
        const newUser: User = {
          id: Math.random().toString(),
          email,
          name,
          role: 'client'
        };
        observer.next(newUser);
        observer.complete();
      }, 1000);
    });
  }
}
