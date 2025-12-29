import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { ClientLayoutComponent } from './core/layouts/client-layout/client-layout.component';
import { OwnerLayoutComponent } from './core/layouts/owner-layout/owner-layout.component';
import { AdminLayoutComponent } from './core/layouts/admin-layout/admin-layout.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'design-demo',
    loadComponent: () => import('./features/design-demo/design-demo.component').then(m => m.DesignDemoComponent)
  },
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
  {
    path: 'home',
    component: ClientLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/logements/home/home.component.js').then(m => m.HomeComponent)
      },
      {
        path: 'logements',
        loadComponent: () => import('./features/logements/listing/listing.component.js').then(m => m.ListingComponent)
      },
      {
        path: 'logements/:id',
        loadComponent: () => import('./features/logements/detail/detail.component.js').then(m => m.DetailComponent)
      },
      {
        path: 'reservations',
        loadComponent: () => import('./features/reservations/reservations/reservations.component.js').then(m => m.ReservationsComponent)
      },
      {
        path: 'favoris',
        loadComponent: () => import('./features/favoris/favoris/favoris.component.js').then(m => m.FavorisComponent)
      },
      {
        path: 'avis',
        loadComponent: () => import('./features/avis/avis/avis.component.js').then(m => m.AvisComponent)
      },
      {
        path: 'paiements',
        loadComponent: () => import('./features/paiements/paiements/paiements.component.js').then(m => m.PaiementsComponent)
      }
    ]
  },
  {
    path: 'proprietaire',
    component: OwnerLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/owner/dashboard/dashboard.component').then(m => m.DashboardComponent)
      },
      {
        path: 'logements',
        loadComponent: () => import('./features/owner/properties/properties.component').then(m => m.PropertiesComponent)
      },
      {
        path: 'reservations',
        loadComponent: () => import('./features/owner/reservations/reservations.component').then(m => m.OwnerReservationsComponent)
      },
      {
        path: 'revenus',
        loadComponent: () => import('./features/owner/earnings/earnings.component').then(m => m.EarningsComponent)
      }
    ]
  },
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/admin/dashboard/dashboard.component').then(m => m.AdminDashboardComponent)
      },
      {
        path: 'utilisateurs',
        loadComponent: () => import('./features/admin/users/users.component').then(m => m.UsersComponent)
      },
      {
        path: 'logements',
        loadComponent: () => import('./features/admin/properties/properties.component').then(m => m.PropertiesComponent)
      },
      {
        path: 'signalements',
        loadComponent: () => import('./features/admin/reports/reports.component').then(m => m.ReportsComponent)
      },
      {
        path: 'statistiques',
        loadComponent: () => import('./features/admin/statistics/statistics.component').then(m => m.StatisticsComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/auth/404'
  }
];
