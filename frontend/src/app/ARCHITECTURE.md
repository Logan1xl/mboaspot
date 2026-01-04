# Architecture MboaSpot Frontend

## Structure du projet

### 📁 `src/app/core/`
Contient la logique transverse et les services partagés au niveau de l'application.

- **`auth/`** - Authentification et autorisation
  - `auth.service.ts` - Service d'authentification
  - `auth.guard.ts` - Garde pour protéger les routes
  - `role.guard.ts` - Garde pour valider les rôles utilisateur

- **`interceptors/`** - Intercepteurs HTTP
  - `jwt.interceptor.ts` - Ajoute le token JWT aux requêtes

- **`services/`** - Services core
  - `api.service.ts` - Service pour les appels API
  - `notification.service.ts` - Gestion des notifications

- **`layouts/`** - Layouts principaux
  - `client-layout/` - Layout pour les clients
  - `owner-layout/` - Layout pour les propriétaires
  - `admin-layout/` - Layout pour les administrateurs

### 📁 `src/app/shared/`
Contient les composants et services réutilisables dans toute l'application.

- **`components/`** - Composants réutilisables
  - `navbar/` - Barre de navigation
  - `footer/` - Pied de page
  - `card-logement/` - Carte de logement
  - `rating/` - Composant d'évaluation
  - `loader/` - Composant de chargement

- **`pipes/`** - Pipes personnalisés
- **`directives/`** - Directives personnalisées

### 📁 `src/app/features/`
Modules fonctionnels de l'application (lazy-loaded).

- **`auth/`** - Authentification (login/register)
- **`logements/`** - Recherche et listing des logements
- **`reservations/`** - Gestion des réservations
- **`paiements/`** - Gestion des paiements
- **`avis/`** - Gestion des avis et commentaires
- **`favoris/`** - Gestion des favoris
- **`owner/`** - Espace propriétaire
- **`admin/`** - Back-office administrateur

### 📁 `src/assets/`
Ressources statiques de l'application.

- **`images/`** - Images du projet
- **`icons/`** - Icônes

### 📁 `src/styles/`
Styles SCSS globaux.

- **`variables.scss`** - Variables SCSS (couleurs, typographie, breakpoints)
- **`theme.scss`** - Définition du thème
- **`layout.scss`** - Styles de layout globaux

### 📁 `src/environments/`
Configuration par environnement.

- **`environment.ts`** - Configuration développement
- **`environment.prod.ts`** - Configuration production

## Principes d'architecture

### Séparation des responsabilités
- **Core** : Logique transverse, services singleton
- **Shared** : Composants et pipes réutilisables
- **Features** : Modules fonctionnels indépendants

### Lazy Loading
Les modules features sont chargés à la demande pour optimiser les performances.

### Réutilisabilité
Les composants shared sont génériques et réutilisables dans plusieurs features.

### Modularité
Chaque feature est autosuffisante et peut être testée indépendamment.

## Utilisation

### Importer le CoreModule dans AppModule
```typescript
import { CoreModule } from './core/core.module';

@NgModule({
  imports: [CoreModule, ...]
})
export class AppModule { }
```

### Importer le SharedModule dans les features
```typescript
import { SharedModule } from '@shared/shared.module';

@NgModule({
  imports: [SharedModule, ...]
})
export class FeatureModule { }
```

## Conventions de nommage

- **Services** : `*.service.ts`
- **Guards** : `*.guard.ts`
- **Interceptors** : `*.interceptor.ts`
- **Composants** : `*.component.ts`, `*.component.html`, `*.component.scss`
- **Modules** : `*.module.ts`
- **Pipes** : `*.pipe.ts`
- **Directives** : `*.directive.ts`

## Prochaines étapes

1. Implémenter les services core (auth, API)
2. Créer les composants shared avec les templates
3. Configurer le routing dans `app.routes.ts`
4. Ajouter les variables d'environnement
5. Développer les modules features
