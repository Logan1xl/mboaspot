import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SearchParams } from '../models/search.model. ts';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  // État partagé de la recherche
  private searchParamsSubject = new BehaviorSubject<SearchParams>({
    city: '',
    priceMin: '',
    priceMax: '',
    propertyType: ''
  });

  searchParams$ = this.searchParamsSubject.asObservable();

  // Mettre à jour les paramètres de recherche
  updateSearchParams(params: SearchParams): void {
    this.searchParamsSubject.next(params);
  }

  // Récupérer les paramètres actuels
  getCurrentParams(): SearchParams {
    return this.searchParamsSubject.value;
  }

  // Réinitialiser les filtres
  resetFilters(): void {
    this.searchParamsSubject.next({
      city: '',
      priceMin: '',
      priceMax: '',
      propertyType: ''
    });
  }
}
