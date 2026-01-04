# Configuration des dépendances

## Dépendances installées

### 1. **Angular Material** (v19)
Framework UI Material Design pour Angular
- Installation: `@angular/material@19`, `@angular/cdk@19`
- Configuration: styles importés dans `styles.scss`
- Thème: `indigo-pink`

### 2. **Bootstrap 5**
Framework CSS responsive
- Installation: `bootstrap`
- Configuration: SCSS importé dans `styles.scss`
- Classe utilitaires disponibles

### 3. **NgX Toastr**
Notifications toast élégantes
- Installation: `ngx-toastr`
- Service: `NotificationService`
- Usage: 
  ```typescript
  constructor(private notif: NotificationService) {}
  
  showSuccess() {
    this.notif.success('Message', 'Titre');
  }
  ```

### 4. **Leaflet & ngx-leaflet**
Cartes interactives
- Installation: `leaflet`, `ngx-leaflet`
- Service: `MapService`
- Features:
  - Initialisation de cartes
  - Gestion des marqueurs
  - Calcul de distances
  - Cercles et zones

### 5. **Géolocalisation HTML5**
API native du navigateur
- Service: `GeolocationService`
- Features:
  - Position actuelle
  - Surveillance en temps réel
  - Calcul de distances
  - Gestion des permissions

### 6. **jsPDF & pdfmake**
Génération de PDF
- Installation: `jspdf`, `pdfmake`
- Service: `PdfService`
- Features:
  - Génération de reçus
  - Tableaux formatés
  - En-têtes personnalisés

## Services configurés

### AuthService
Gestion complète de l'authentification
```typescript
login(email, password): Observable<AuthToken>
logout(): void
getCurrentUser(): User | null
isLoggedIn(): boolean
refreshToken(): Observable<AuthToken>
register(email, password, name): Observable<User>
```

### ApiService
Client HTTP avec gestion automatique des tokens
```typescript
get<T>(endpoint, params?): Observable<T>
post<T>(endpoint, data): Observable<T>
put<T>(endpoint, data): Observable<T>
patch<T>(endpoint, data): Observable<T>
delete<T>(endpoint): Observable<T>
```

### NotificationService
Notifications toast
```typescript
success(message, title?): void
error(message, title?): void
info(message, title?): void
warning(message, title?): void
show(message, type, title?): void
```

### MapService
Gestion des cartes Leaflet
```typescript
initializeMap(elementId, config): L.Map
addMarker(id, marker): L.Marker
removeMarker(id): void
clearMarkers(): void
centerMap(lat, lng, zoom?): void
fitBoundsToMarkers(): void
addCircle(lat, lng, radius, options?): void
```

### GeolocationService
Géolocalisation GPS
```typescript
getCurrentPosition(enableHighAccuracy?): Observable<GeoLocation>
watchPosition(enableHighAccuracy?): Observable<GeoLocation>
calculateDistance(from, to): number
```

### PdfService
Génération de PDF
```typescript
generateReceipt(receipt): void
generateSimplePdf(title, content, fileName): void
```

## Garde d'authentification

### authGuard
Protège les routes authentifiées
```typescript
{
  path: 'protected',
  component: MyComponent,
  canActivate: [authGuard]
}
```

### roleGuard
Protège les routes par rôle
```typescript
{
  path: 'admin',
  component: AdminComponent,
  canActivate: [roleGuard(['admin'])]
}
```

## Intercepteur JWT

Ajoute automatiquement le token JWT aux requêtes HTTP
- Gestion automatique des tokens expirés
- Rafraîchissement automatique du token
- Déconnexion automatique si le refresh échoue

## Routes configurées

### Client Layout (/home)
- Home page
- Logements (listing, détail)
- Réservations
- Favoris
- Avis
- Paiements

### Owner Layout (/proprietaire)
- Dashboard
- Mes logements
- Réservations
- Revenus

### Admin Layout (/admin)
- Dashboard
- Utilisateurs
- Logements
- Signalements
- Statistiques

## Configuration de l'environnement

### development (environment.ts)
```typescript
production: false
apiUrl: 'http://localhost:8003/api'
```

### production (environment.prod.ts)
```typescript
production: true
apiUrl: 'https://api.example.com/api'
```

## Prochaines étapes

1. ✅ Installer les dépendances
2. ✅ Configurer Angular Material
3. ✅ Configurer Bootstrap
4. ✅ Créer les services
5. ✅ Configurer le routing avec rôles
6. ⏳ Créer les composants des pages
7. ⏳ Intégrer les API réelles
8. ⏳ Tests e2e et unitaires

## Installation des dépendances TypeScript

Pour typage complet de leaflet:
```bash
npm install --save-dev @types/leaflet
```

Pour typage complet de jsPDF:
```bash
npm install --save-dev @types/jspdf
```
