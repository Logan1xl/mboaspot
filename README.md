mboaspot/
│
├── backend/                          # 🔴 PHASE ACTUELLE
│   ├── src/
│   │   ├── main/java/cm/logement/
│   │   │   ├── HousingApplication.java
│   │   │   ├── config/               # Configuration Spring
│   │   │   │   ├── JwtConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── CorsConfig.java
│   │   │   ├── controller/           # API Endpoints
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AnnoncesController.java
│   │   │   │   ├── SearchController.java
│   │   │   │   ├── ReservationController.java
│   │   │   │   ├── PaiementController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── service/              # Logique métier
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── AnnoncesService.java
│   │   │   │   ├── SearchService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   ├── PaiementService.java
│   │   │   │   └── NotificationService.java
│   │   │   ├── repository/           # Accès données
│   │   │   │   ├── UtilisateurRepository.java
│   │   │   │   ├── AnnoncesRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   ├── PaiementRepository.java
│   │   │   │   └── FavoriRepository.java
│   │   │   ├── entity/               # Entités JPA
│   │   │   │   ├── Utilisateur.java
│   │   │   │   ├── Annonces.java
│   │   │   │   ├── Reservation.java
│   │   │   │   ├── Paiement.java
│   │   │   │   ├── Voyageur.java
│   │   │   │   ├── Proprietaire.java
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Avis.java
│   │   │   │   ├── Favori.java
│   │   │   │   ├── Equipement.java
│   │   │   │   ├── Disponibilite.java
│   │   │   │   ├── Localisation.java
│   │   │   │   ├── Signalement.java
│   │   │   │   ├── Notification.java
│   │   │   │   └── Paiement.java
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   ├── CreateAnnoncesRequest.java
│   │   │   │   │   ├── SearchRequest.java
│   │   │   │   │   ├── ReservationRequest.java
│   │   │   │   │   └── PaiementRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── AnnoncesResponse.java
│   │   │   │       ├── ReservationResponse.java
│   │   │   │       └── ApiResponse.java
│   │   │   ├── exception/            # Exception personnalisées
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── security/             # Sécurité
│   │   │   │   ├── JwtProvider.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   ├── util/                 # Utilitaires
│   │   │   │   ├── Constants.java
│   │   │   │   ├── ValidationUtil.java
│   │   │   │   └── PdfUtil.java
│   │   │   └── mapper/               # MapStruct mappers
│   │   │       ├── UtilisateurMapper.java
│   │   │       ├── AnnoncesMapper.java
│   │   │       └── ReservationMapper.java
│   │   │
│   │   ├── test/java/cm/logement/
│   │   │   ├── service/
│   │   │   │   ├── AuthServiceTest.java
│   │   │   │   └── AnnoncesServiceTest.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthControllerTest.java
│   │   │   │   └── AnnoncesControllerTest.java
│   │   │   └── repository/
│   │   │       └── UtilisateurRepositoryTest.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml        # Config principale
│   │       ├── application-dev.yml    # Développement
│   │       ├── application-test.yml   # Tests
│   │       ├── application-prod.yml   # Production
│   │       ├── db/migration/          # Flyway/Liquibase
│   │       │   └── V1__Initial_schema.sql
│   │       └── logback-spring.xml
│   │
│   ├── pom.xml                        # Maven dependencies
│   ├── Dockerfile                     # Containerisation
│   ├── .dockerignore
│   └── README.md
│
├── frontend/                          # 🟢 PHASE 2 (React/Angular)
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── store/
│   │   └── App.jsx
│   ├── package.json
│   └── Dockerfile
│
├── mobile/                            # 🟡 PHASE 3 (Flutter)
│   ├── lib/
│   │   ├── screens/
│   │   ├── services/
│   │   └── main.dart
│   ├── pubspec.yaml
│   └── Dockerfile
│
├── devops/                            # DevOps
│   ├── docker-compose.yml
│   ├── kubernetes/
│   ├── nginx/
│   └── monitoring/
│
├── docs/
│   ├── API.md                         # Documentation API
│   ├── DATABASE.md                    # Schéma BD
│   ├── ARCHITECTURE.md                # Architecture
│   └── SETUP.md                       # Guide d'installation
│
├── .github/
│   ├── workflows/
│   │   ├── ci-backend.yml
│   │   ├── ci-frontend.yml
│   │   └── cd-deploy.yml
│   ├── ISSUE_TEMPLATE/
│   │   ├── bug_report.md
│   │   └── feature_request.md
│   └── pull_request_template.md
│
├── .gitignore
├── README.md                          # Vue d'ensemble
├── CONTRIBUTING.md                    # Guide contribution
└── CODE_OF_CONDUCT.md
