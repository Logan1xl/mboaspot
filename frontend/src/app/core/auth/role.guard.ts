// src/app/core/auth/role.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const roleGuard = (allowedRoles: string[]): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isAuthenticated()) {
      router.navigate(['/auth/login']);
      return false;
    }

    if (authService.hasRole(allowedRoles)) {
      return true;
    }

    // Rediriger vers une page d'accès refusé ou home
    router.navigate(['/']);
    return false;
  };
};
