import { Routes } from '@angular/router';
import {HomeComponent} from './features/logements/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: '**', redirectTo: '' }
];
