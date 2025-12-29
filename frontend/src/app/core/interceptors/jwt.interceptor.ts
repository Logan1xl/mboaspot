import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Ajouter le token JWT à la requête
    const token = localStorage.getItem('accessToken');
    if (token && !request.url.includes('login') && !request.url.includes('register')) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expiré - essayer de rafraîchir
          return this.authService.refreshToken().pipe(
            switchMap((response: any) => {
              const newToken = response.accessToken;
              localStorage.setItem('accessToken', newToken);

              // Réessayer la requête avec le nouveau token
              const clonedRequest = request.clone({
                setHeaders: {
                  Authorization: `Bearer ${newToken}`
                }
              });

              return next.handle(clonedRequest);
            }),
            catchError(() => {
              // Si le rafraîchissement échoue, déconnecter l'utilisateur
              this.authService.logout();
              return throwError(() => error);
            })
          );
        }

        return throwError(() => error);
      })
    );
  }
}

