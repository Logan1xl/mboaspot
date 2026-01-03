import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'listing', loadComponent: () => import('./components/listing/listing.component').then(m => m.ListingComponent) },
  { path: '**', redirectTo: '' }
];
