export interface Annonce {
  id: number;
  titre: string;
  prix: number;
  adresse: string;
  ville: string;
  nbreChambres: number;
  nbreLits: number;
  maxInvites: number;
  urlImagePrincipale: string;
  urlImages: string;
  description: string;
  evaluationMoyenne: number;
  totalAvis: number;
  // Ces objets correspondent aux relations @ManyToOne et @ManyToMany de ton Java
  idProprietaire?: {
    nom: string;
    phone: string;
  };
  equipementList?: {
    nom: string;
    icone: string;
  }[];
}
 export interface Avis {
    id: number;
    auteur: string;
    commentaire: string;
    note: number;
    date: string;
    
}