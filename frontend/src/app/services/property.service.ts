import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Property {
  id: number;
  title: string;
  price: number;
  city: string;
  type: string;
  imageUrl: string;
}

@Injectable({
  providedIn: 'root'
})
export class PropertyService {
  private apiUrl = 'http://localhost:3000/api/properties';

  constructor(private http: HttpClient) {}

  // Récupérer toutes les propriétés
  getAllProperties(): Observable<Property[]> {
    return this.http.get<Property[]>(this.apiUrl);
  }

  // Récupérer une propriété par ID
  getPropertyById(id: number): Observable<Property> {
    return this.http.get<Property>(`${this.apiUrl}/${id}`);
  }

  // Rechercher avec filtres
  searchProperties(filters: any): Observable<Property[]> {
    let params = new HttpParams();

    if (filters.city) params = params.set('city', filters.city);
    if (filters.priceMin) params = params.set('priceMin', filters.priceMin);
    if (filters.priceMax) params = params.set('priceMax', filters.priceMax);
    if (filters.type) params = params.set('type', filters.type);

    return this.http.get<Property[]>(`${this.apiUrl}/search`, { params });
  }

  // Créer une nouvelle propriété
  createProperty(property: Property): Observable<Property> {
    return this.http.post<Property>(this.apiUrl, property);
  }

  // Mettre à jour une propriété
  updateProperty(id: number, property: Property): Observable<Property> {
    return this.http.put<Property>(`${this.apiUrl}/${id}`, property);
  }

  // Supprimer une propriété
  deleteProperty(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
