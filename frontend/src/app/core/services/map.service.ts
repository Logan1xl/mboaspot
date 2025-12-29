import { Injectable } from '@angular/core';
import * as L from 'leaflet';

export interface MapConfig {
  center: [number, number];
  zoom: number;
  dragging?: boolean;
  scrollWheelZoom?: boolean;
}

export interface Marker {
  lat: number;
  lng: number;
  title: string;
  icon?: L.Icon;
  popup?: string;
}

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private map: L.Map | null = null;
  private markers: Map<string, L.Marker> = new Map();

  constructor() {
    this.initializeLeaflet();
  }

  /**
   * Initialise les icônes par défaut de Leaflet
   */
  private initializeLeaflet(): void {
    L.Icon.Default.mergeOptions({
      iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png'
    });
  }

  /**
   * Initialise la carte
   */
  initializeMap(elementId: string, config: MapConfig): L.Map {
    if (this.map) {
      this.map.remove();
    }

    this.map = L.map(elementId).setView(config.center, config.zoom);

    // Couche de tuiles OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 19
    }).addTo(this.map);

    // Configuration
    if (config.dragging !== undefined) {
      this.map.dragging.disable();
    }
    if (config.scrollWheelZoom !== undefined) {
      this.map.scrollWheelZoom.disable();
    }

    return this.map;
  }

  /**
   * Ajoute un marqueur à la carte
   */
  addMarker(id: string, marker: Marker): L.Marker | null {
    if (!this.map) return null;

    const markerObj = L.marker([marker.lat, marker.lng], {
      icon: marker.icon || L.icon({
        iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
        shadowSize: [41, 41]
      })
    }).addTo(this.map);

    if (marker.popup) {
      markerObj.bindPopup(marker.popup);
    } else if (marker.title) {
      markerObj.bindPopup(marker.title);
    }

    markerObj.bindTooltip(marker.title);
    this.markers.set(id, markerObj);

    return markerObj;
  }

  /**
   * Supprime un marqueur
   */
  removeMarker(id: string): void {
    const marker = this.markers.get(id);
    if (marker) {
      this.map?.removeLayer(marker);
      this.markers.delete(id);
    }
  }

  /**
   * Efface tous les marqueurs
   */
  clearMarkers(): void {
    this.markers.forEach(marker => {
      this.map?.removeLayer(marker);
    });
    this.markers.clear();
  }

  /**
   * Centre la carte sur une position
   */
  centerMap(lat: number, lng: number, zoom?: number): void {
    if (this.map) {
      this.map.setView([lat, lng], zoom);
    }
  }

  /**
   * Obtient la carte
   */
  getMap(): L.Map | null {
    return this.map;
  }

  /**
   * Ajoute un cercle
   */
  addCircle(lat: number, lng: number, radius: number, options?: L.CircleMarkerOptions): void {
    if (this.map) {
      L.circle([lat, lng], {
        radius: radius,
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5,
        ...options
      }).addTo(this.map);
    }
  }

  /**
   * Calcule les limites pour afficher tous les marqueurs
   */
  fitBoundsToMarkers(): void {
    if (this.map && this.markers.size > 0) {
      const group = new L.FeatureGroup(Array.from(this.markers.values()));
      this.map.fitBounds(group.getBounds());
    }
  }
}
