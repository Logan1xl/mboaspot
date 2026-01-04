
export interface User {
  id: number;
  email: string;
  nom: string;
  prenom: string;
  numeroTelephone?: string;
  photoProfil?: string;
  role: 'ADMIN' | 'PROPRIETAIRE' | 'VOYAGEUR';
  estActif: boolean;
}

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  userId: number;
  email: string;
  nom: string;
  prenom: string;
  role: 'ADMIN' | 'PROPRIETAIRE' | 'VOYAGEUR';
  numeroTelephone?: string;
  photoProfil?: string;
}

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface RegisterRequest {
  nom: string;
  prenom: string;
  email: string;
  motDePasse: string;
  numeroTelephone?: string;
  photoProfil?: string;
  role: 'VOYAGEUR' | 'PROPRIETAIRE';
  // Champs spécifiques propriétaire
  nomEntreprise?: string;
  numeroIdentification?: string;
  compteBancaire?: string;
  // Champs spécifiques voyageur
  preferences?: string;
}

export interface Annonce {
  id: number;
  titre: string;
  prix: number;
  adresse: string;
  ville: string;
  latitude?: number;
  longitude?: number;
  nbreChambres: number;
  nbreLits: number;
  maxInvites: number;
  description: string;
  typeAnnonce: string;
  urlImagePrincipale?: string;
  urlImages?: string[];
  estActive: boolean;
  evaluationMoyenne: number;
  totalAvis: number;
  idProprietaire?: number;
  proprietaireNom?: string;
  localisation?: Localisation;
}

export interface Localisation {
  id?: number;
  ville: string;
  quartier: string;
  latitude?: number;
  longitude?: number;
  idAnnonce?: number;
}

export interface Equipement {
  id?: number;
  nom: string;
  icone?: string;
  description?: string;
}

export interface Disponibilite {
  id?: number;
  estDisponible: boolean;
  prixSurcharge: number;
  dateDebut: Date;
  dateFin: Date;
  idAnnonce: number;
}

export interface RechercheFilters {
  ville?: string;
  quartier?: string;
  typeAnnonce?: string;
  prixMin?: number;
  prixMax?: number;
  nbreChambres?: number;
  evaluationMin?: number;
  latitude?: number;
  longitude?: number;
  rayon?: number;
  page?: number;
  size?: number;
  sort?: string;
}

export interface Reservation {
  id: number;
  codeConfirmation: string;
  nombreInvites: number;
  prixTotal: number;
  statut: 'EN_ATTENTE' | 'CONFIRMEE' | 'ANNULEE' | 'TERMINEE';
  dateDebut?: Date;
  dateFin?: Date;
  annonceId: number;
  annonceTitre?: string;
  voyageurId: number;
  voyageurNom?: string;
}

export interface ReservationRequest {
  annonceId: number;
  voyageurId: number;
  dateDebut: Date;
  dateFin: Date;
  nombreInvites: number;
}

export interface Paiement {
  id: number;
  montant: number;
  idTransaction: string;
  urlRecepisse?: string;
  methode: 'MTN' | 'ORANGE';
  statut: 'EN_ATTENTE' | 'VALIDE' | 'ECHOUE';
  idReservation: number;
  codeConfirmationReservation?: string;
  nomVoyageur?: string;
}

export interface PaiementRequest {
  reservationId: number;
  montant: number;
  methode: 'MTN' | 'ORANGE';
  phoneNumber: string;
}

export interface Favori {
  id?: number;
  idVoyageur: number;
  idAnnonce: number;
}

export interface Avis {
  id?: number;
  note: number;
  photos?: string;
  estSignale: boolean;
  raisonSignalement?: string;
  idVoyageur: number;
}

export interface Notification {
  id?: number;
  titre: string;
  message: string;
  estLue: boolean;
  typeNotification?: string;
  idUser: number;
}

export interface Signalement {
  id?: number;
  raison: string;
  description: string;
  resolution?: string;
  statut: 'EN_ATTENTE' | 'TRAITE' | 'REJETE';
  idAnnonce: number;
  idAdmin: number;
}

export interface DashboardStats {
  totalAnnonces?: number;
  totalReservations?: number;
  revenuTotal?: number;
  tauxOccupation?: number;
  totalUsers?: number;
  annoncesRecentes?: Annonce[];
  villesPopulaires?: string[];
  activite7j?: any[];
  revenus30j?: any[];
}
