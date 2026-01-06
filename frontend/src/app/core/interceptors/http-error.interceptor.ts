// http-error.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {NotifService} from '../services/notif-service.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private notificationService: NotifService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      retry(1), // Réessaie une fois en cas d'échec
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'Une erreur est survenue';

        if (error.error instanceof ErrorEvent) {
          // Erreur côté client
          errorMessage = `Erreur: ${error.error.message}`;
        } else {
          // Erreur côté serveur
          switch (error.status) {
            case 0:
              errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion.';
              break;
            case 400:
              errorMessage = error.error?.message || 'Requête invalide';
              break;
            case 401:
              errorMessage = 'Non autorisé. Veuillez vous connecter.';
              break;
            case 403:
              errorMessage = 'Accès refusé';
              break;
            case 404:
              errorMessage = 'Ressource non trouvée';
              break;
            case 500:
              errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
              break;
            case 503:
              errorMessage = 'Service temporairement indisponible';
              break;
            default:
              errorMessage = error.error?.message || `Erreur ${error.status}`;
          }
        }

        console.error('HTTP Error:', error);

        // N'affiche pas de notification pour certaines requêtes (comme le comptage)
        if (!request.url.includes('/statistiques') && error.status !== 404) {
          this.notificationService.error(errorMessage);
        }

        return throwError(() => error);
      })
    );
  }
}
