// src/app/shared/components/map/map.component.ts
import { Component, OnInit, Input, Output, EventEmitter, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

// Fix pour les icônes Leaflet avec webpack
const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div [id]="mapId" class="map-container" [style.height]="height"></div>
  `,
  styles: [`
    .map-container {
      width: 100%;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
  `]
})
export class MapComponent implements OnInit, AfterViewInit {
  @Input() latitude: number = 4.0511; // Douala par défaut
  @Input() longitude: number = 9.7679;
  @Input() zoom: number = 13;
  @Input() height: string = '400px';
  @Input() markers: Array<{lat: number, lng: number, title?: string, data?: any}> = [];
  @Input() interactive: boolean = true; // Permettre de cliquer pour placer un marker
  @Output() locationSelected = new EventEmitter<{lat: number, lng: number}>();

  mapId: string = 'map-' + Math.random().toString(36).substr(2, 9);
  private map!: L.Map;
  private userMarker?: L.Marker;

  // Villes principales du Cameroun
  private cameroonCities = {
    douala: { lat: 4.0511, lng: 9.7679, zoom: 12 },
    yaounde: { lat: 3.8480, lng: 11.5021, zoom: 12 },
    bafoussam: { lat: 5.4781, lng: 10.4177, zoom: 13 },
    garoua: { lat: 9.3012, lng: 13.3964, zoom: 13 },
    bamenda: { lat: 5.9631, lng: 10.1591, zoom: 13 }
  };

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    setTimeout(() => this.initMap(), 100);
  }

  private initMap(): void {
    // Créer la carte centrée sur le Cameroun
    this.map = L.map(this.mapId, {
      center: [this.latitude, this.longitude],
      zoom: this.zoom,
      zoomControl: true,
      attributionControl: true
    });

    // Ajouter la couche de tuiles OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    // Ajouter les markers existants
    this.addMarkers();

    // Si mode interactif, permettre de placer un marker en cliquant
    if (this.interactive) {
      this.map.on('click', (e: L.LeafletMouseEvent) => {
        this.placeUserMarker(e.latlng.lat, e.latlng.lng);
      });
    }

    // Si latitude/longitude fournis, placer un marker initial
    if (this.interactive && this.latitude && this.longitude) {
      this.placeUserMarker(this.latitude, this.longitude);
    }
  }

  private addMarkers(): void {
    if (this.markers.length > 0) {
      this.markers.forEach(marker => {
        const leafletMarker = L.marker([marker.lat, marker.lng])
          .addTo(this.map);

        if (marker.title) {
          leafletMarker.bindPopup(marker.title);
        }

        // Clic sur marker pour émettre les données
        if (marker.data) {
          leafletMarker.on('click', () => {
            // Peut être utilisé pour naviguer vers le détail
          });
        }
      });

      // Ajuster la vue pour afficher tous les markers
      if (this.markers.length > 1) {
        const bounds = L.latLngBounds(this.markers.map(m => [m.lat, m.lng]));
        this.map.fitBounds(bounds, { padding: [50, 50] });
      }
    }
  }

  private placeUserMarker(lat: number, lng: number): void {
    // Supprimer le marker précédent s'il existe
    if (this.userMarker) {
      this.map.removeLayer(this.userMarker);
    }

    // Créer un nouveau marker rouge pour l'utilisateur
    const redIcon = L.icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
      shadowUrl: 'assets/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });

    this.userMarker = L.marker([lat, lng], { icon: redIcon })
      .addTo(this.map)
      .bindPopup('Emplacement sélectionné')
      .openPopup();

    // Émettre la position
    this.locationSelected.emit({ lat, lng });
  }

  // Méthode publique pour centrer la carte sur une ville
  public centerOnCity(cityName: string): void {
    const city = this.cameroonCities[cityName.toLowerCase() as keyof typeof this.cameroonCities];
    if (city && this.map) {
      this.map.setView([city.lat, city.lng], city.zoom);
    }
  }

  // Méthode publique pour ajouter un marker
  public addMarker(lat: number, lng: number, title?: string): void {
    if (this.map) {
      L.marker([lat, lng])
        .addTo(this.map)
        .bindPopup(title || 'Marker');
    }
  }
}
