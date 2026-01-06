// src/app/core/services/upload.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private apiUrl = `${environment.apiUrl}/upload`;

  constructor(private http: HttpClient) {}

  uploadImage(file: File): Observable<{url: string, fileName: string}> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{url: string, fileName: string}>(`${this.apiUrl}/image`, formData);
  }

  uploadMultipleImages(files: File[]): Observable<{uploaded: any[], errors: any[]}> {
    const formData = new FormData();
    files.forEach(file => formData.append('files', file));
    return this.http.post<{uploaded: any[], errors: any[]}>(`${this.apiUrl}/images`, formData);
  }

  deleteImage(fileName: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/image/${fileName}`);
  }
}
