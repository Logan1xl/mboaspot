import { Injectable } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { inject } from '@angular/core';

export const roleGuard = (requiredRoles: string[]): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const currentUser = authService.getCurrentUser();
    if (currentUser && requiredRoles.includes(currentUser.role)) {
      return true;
    }

    router.navigate(['/auth/unauthorized']);
    return false;
  };
};

@Injectable({
  providedIn: 'root'
})
export class RoleGuard {

  constructor(private authService: AuthService, private router: Router) { }

  /**
   * Vérifie si l'utilisateur a un rôle spécifique
   */
  hasRole(role: string | string[]): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return false;

    if (Array.isArray(role)) {
      return role.includes(currentUser.role);
    }
    return currentUser.role === role;
  }

  /**
   * Vérifie si l'utilisateur a tous les rôles spécifiés
   */
  hasAllRoles(roles: string[]): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return false;

    return roles.every(role => role === currentUser.role);
  }

  /**
   * Vérifie si l'utilisateur a au moins un des rôles spécifiés
   */
  hasAnyRole(roles: string[]): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return false;

    return roles.some(role => role === currentUser.role);
  }
}
