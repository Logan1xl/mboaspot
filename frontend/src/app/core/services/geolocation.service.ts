import { Injectable } from '@angular/core';
import { Observable, from } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface Coordinates {
  latitude: number;
  longitude: number;
}

export interface GeoLocation extends Coordinates {
  accuracy: number;
  altitude: number | null;
  altitudeAccuracy: number | null;
  heading: number | null;
  speed: number | null;
  timestamp: number;
}

@Injectable({
  providedIn: 'root'
})
export class GeolocationService {

  constructor() { }

  /**
   * Obtient la position actuelle de l'utilisateur
   */
  getCurrentPosition(enableHighAccuracy = true): Observable<GeoLocation> {
    return from(
      new Promise<GeoLocation>((resolve, reject) => {
        if (!navigator.geolocation) {
          reject(new Error('Géolocalisation non disponible'));
        } else {
          navigator.geolocation.getCurrentPosition(
            (position) => {
              resolve({
                latitude: position.coords.latitude,
                longitude: position.coords.longitude,
                accuracy: position.coords.accuracy,
                altitude: position.coords.altitude,
                altitudeAccuracy: position.coords.altitudeAccuracy,
                heading: position.coords.heading,
                speed: position.coords.speed,
                timestamp: position.timestamp
              });
            },
            (error) => {
              reject(error);
            },
            { enableHighAccuracy, timeout: 10000, maximumAge: 0 }
          );
        }
      })
    );
  }

  /**
   * Observe la position en temps réel
   */
  watchPosition(enableHighAccuracy = true): Observable<GeoLocation> {
    return new Observable((observer) => {
      if (!navigator.geolocation) {
        observer.error(new Error('Géolocalisation non disponible'));
        return;
      } else {
        const watchId = navigator.geolocation.watchPosition(
          (position) => {
            observer.next({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              accuracy: position.coords.accuracy,
              altitude: position.coords.altitude,
              altitudeAccuracy: position.coords.altitudeAccuracy,
              heading: position.coords.heading,
              speed: position.coords.speed,
              timestamp: position.timestamp
            });
          },
          (error) => {
            observer.error(error);
          },
          { enableHighAccuracy, timeout: 10000, maximumAge: 0 }
        );

        return () => {
          navigator.geolocation.clearWatch(watchId);
        };
      }
    });
  }

  /**
   * Calcule la distance entre deux points géographiques (en km)
   */
  calculateDistance(from: Coordinates, to: Coordinates): number {
    const R = 6371; // Rayon de la Terre en km
    const dLat = this.toRad(to.latitude - from.latitude);
    const dLng = this.toRad(to.longitude - from.longitude);
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
              Math.cos(this.toRad(from.latitude)) * Math.cos(this.toRad(to.latitude)) *
              Math.sin(dLng / 2) * Math.sin(dLng / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
  }

  /**
   * Convertit les degrés en radians
   */
  private toRad(value: number): number {
    return value * Math.PI / 180;
  }
}
