# Mboaspot

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.2.1.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
# MboaSpot — Frontend

Petit guide pratique pour l'équipe (Logan, Johann, Carmelle, Lumière).

Résumé
------
Application Angular servant d'interface utilisateur pour rechercher, réserver et gérer logements.

Structure principale (src/app)
- `core/` : services partagés, guards, interceptors, layouts.
- `shared/` : composants réutilisables (cards, navbar, footer, loader, rating).
- `features/` : modules métier (auth, logements, reservations, paiements, avis, favoris, admin, owner).
- `assets/` : images/icônes.
- `environments/` : variables d'environnement (dev/prod).

Commandes utiles
-----------------
Installer et lancer en dev :
```bash
npm install
npm start
```
Builder (prod) :
```bash
npm run build
```
Tests unitaires :
```bash
npm test
```

Travailler ensemble
-------------------
- Branches : `feature/xxx`, PR -> code review avant merge.
- Commit message : `type(scope): message` (feat, fix, docs, test, ci).
- Standups : Lundi 10:00, Jeudi 18:00.

Conventions rapides
-------------------
- Services : `*.service.ts`
- Components : `*.component.ts` (+ `.html`, `.scss`, `.spec.ts`)
- Guards : `*.guard.ts`
- Interceptors : `*.interceptor.ts`
- Models/DTOs : `*.model.ts` ou `*.dto.ts`

API backend attendu (raccourci pour dev front)
-------------------------------------------
Base URL attendu : `http://localhost:8080/api`

Auth
- `POST /api/auth/login` — login
- `POST /api/auth/register` — register
- `GET  /api/auth/me` — user courant

Annonces
- `GET  /api/annonces` — liste paginée
- `GET  /api/annonces/{id}` — détail
- `POST /api/annonces` — créer (form-data si images)
- `POST /api/annonces/recherche` — recherche avancée

Réservations
- `POST /api/reservations` — créer réservation
- `PATCH /api/reservations/{id}/cancel` — annuler
- `PATCH /api/reservations/{id}/confirm` — confirmer

Paiements (sandbox)
- `POST /api/payments/mtn/initiate` — initier paiement MTN
- `GET  /api/payments/{transactionId}/status` — statut

Avis / Favoris / Notifications
- `POST /api/avis` — créer avis
- `POST /api/favoris` — ajouter favori
- `GET  /api/notifications?user={id}` — lister notifications

Tâches prioritaires (court terme)
--------------------------------
1. Auth (login/register, guards) — Carmelle + Johann
2. Layouts & navbar responsive — Carmelle
3. Endpoint `GET /api/auth/me` et users CRUD — Johann
4. Dockerize frontend + CI simple — Lumière

Prochaine étape
---------------
Je peux générer un `CONTRIBUTING.md` et un petit `TASKS_BREAKDOWN.md` par feature avec tickets assignés à chacun. Voulez-vous que je le fasse maintenant ?

---
*Dernière mise à jour : 29 Dec 2025*
