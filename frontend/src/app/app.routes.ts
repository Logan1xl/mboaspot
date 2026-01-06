// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { roleGuard } from './core/auth/role.guard';


export const routes: Routes = [
  // Redirection par défaut
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },

  // Routes publiques
  {
    path: 'home',
    loadComponent: () => import('./features/logements/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'logements',
    loadComponent: () => import('./features/logements/listing/listing.component').then(m => m.ListingComponent)
  },
  {
    path: 'logements/:id',
    loadComponent: () => import('./features/logements/detail/detail.component').then(m => m.DetailComponent)
  },

  // Routes d'authentification
  {
    path: 'auth',
    children: [
      {
        path: 'login',
        loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
      },
       {
        path: 'unauthorized',
        loadComponent: () => import('./features/auth/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent)
      },
      {
        path: '404',
        loadComponent: () => import('./features/auth/not-found/not-found.component').then(m => m.NotFoundComponent)
      }
    ]
  },

  // Routes protégées - Voyageur
  {
    path: 'mes-reservations',
    loadComponent: () => import('./features/reservations/reservations/reservations.component').then(m => m.MesReservationsComponent),
    canActivate: [authGuard, roleGuard(['VOYAGEUR'])]
  },
  {
    path: 'mes-favoris',
    loadComponent: () => import('./features/favoris/favoris/favoris.component').then(m => m.FavorisComponent),
    canActivate: [authGuard, roleGuard(['VOYAGEUR'])]
  },

  // Routes protégées - Propriétaire
  {
    path: 'owner',
    canActivate: [authGuard, roleGuard(['PROPRIETAIRE'])],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/owner/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'annonces',
        loadComponent: () => import('./features/owner/mes-annonces/mes-annonces.component').then(m => m.MesAnnoncesComponent)
      }
    ]
  },

  // Routes protégées - Admin
  {
    path: 'admin',
    canActivate: [authGuard, roleGuard(['ADMIN'])],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/admin/dashboard/dashboard.component').then(m => m.AdminDashboardComponent)
      },
      {
        path: 'users',
        loadComponent: () => import('./features/admin/users/users.component').then(m => m.UsersComponent)
      },


    ]
  },

  // Page 404
  {
    path: '**',
    redirectTo: '/auth/404'}
];
