import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Annonce, Avis } from '../models/annonce';

@Injectable({ providedIn: 'root' })
export class AnnonceService {
  private readonly API_URL = 'http://localhost:8080/api'; // Vérifie ton port Spring Boot

  constructor(private http: HttpClient) {}

  // Correspond à GET /api/annonces/{id}
  getAnnonceById(id: number): Observable<Annonce> {
    return this.http.get<Annonce>(`${this.API_URL}/annonces/${id}`);
  }

  // Correspond à GET /api/avis?annonce={id}
  getAvisByAnnonce(id: number): Observable<Avis[]> {
    return this.http.get<Avis[]>(`${this.API_URL}/avis?annonce=${id}`);
  }
}