package com.example.backend.dto;

import java.util.List;

public class AnnonceDTO {
    private Long id;
    private String titre;
    private Double prix;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private String ville;
    private String quartier; // Ajouté depuis les besoins de localisation
    private Integer nbreChambres;
    private Integer nbreLits;
    private Integer maxInvites;
    private String description;
    private String typeAnnonce;
    private Boolean estActive;
    private Double evaluationMoyenne;
    private Integer totalAvis;
    private String urlImagePrincipale;
    private List<String> urlImages;

    // Informations du propriétaire
    private Long idProprietaire;
    private String proprietaireNom; // Anciennement nomProprietaire, plus cohérent
    private String proprietaireEntreprise;

    // Relations et objets complexes
    private List<EquipementDTO> equipements;
    private LocalisationDTO localisation;
    private List<DisponibiliteDTO> disponibilites;

    // Pour les recherches et statistiques
    private Double prixSurcharge; // Pour les périodes spéciales
    private Double distance; // Pour les recherches par géolocalisation

    // Constructeurs
    public AnnonceDTO() {}

    // Constructeur simplifié pour création rapide
    public AnnonceDTO(String titre, Double prix, String adresse, String ville,
                      Integer nbreChambres, Integer nbreLits, Integer maxInvites,
                      String description, String typeAnnonce, Long idProprietaire) {
        this.titre = titre;
        this.prix = prix;
        this.adresse = adresse;
        this.ville = ville;
        this.nbreChambres = nbreChambres;
        this.nbreLits = nbreLits;
        this.maxInvites = maxInvites;
        this.description = description;
        this.typeAnnonce = typeAnnonce;
        this.idProprietaire = idProprietaire;
        this.estActive = true; // Par défaut active
        this.evaluationMoyenne = 0.0;
        this.totalAvis = 0;
    }

    // Constructeur complet
    public AnnonceDTO(Long id, String titre, Double prix, String adresse,
                      Double latitude, Double longitude, String ville, String quartier,
                      Integer nbreChambres, Integer nbreLits, Integer maxInvites,
                      String description, String typeAnnonce, Boolean estActive,
                      Double evaluationMoyenne, Integer totalAvis, String urlImagePrincipale,
                      Long idProprietaire, String proprietaireNom, String proprietaireEntreprise) {
        this.id = id;
        this.titre = titre;
        this.prix = prix;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ville = ville;
        this.quartier = quartier;
        this.nbreChambres = nbreChambres;
        this.nbreLits = nbreLits;
        this.maxInvites = maxInvites;
        this.description = description;
        this.typeAnnonce = typeAnnonce;
        this.estActive = estActive;
        this.evaluationMoyenne = evaluationMoyenne;
        this.totalAvis = totalAvis;
        this.urlImagePrincipale = urlImagePrincipale;
        this.idProprietaire = idProprietaire;
        this.proprietaireNom = proprietaireNom;
        this.proprietaireEntreprise = proprietaireEntreprise;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public Integer getNbreChambres() {
        return nbreChambres;
    }

    public void setNbreChambres(Integer nbreChambres) {
        this.nbreChambres = nbreChambres;
    }

    public Integer getNbreLits() {
        return nbreLits;
    }

    public void setNbreLits(Integer nbreLits) {
        this.nbreLits = nbreLits;
    }

    public Integer getMaxInvites() {
        return maxInvites;
    }

    public void setMaxInvites(Integer maxInvites) {
        this.maxInvites = maxInvites;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public Boolean getEstActive() {
        return estActive;
    }

    public void setEstActive(Boolean estActive) {
        this.estActive = estActive;
    }

    public Double getEvaluationMoyenne() {
        return evaluationMoyenne;
    }

    public void setEvaluationMoyenne(Double evaluationMoyenne) {
        this.evaluationMoyenne = evaluationMoyenne;
    }

    public Integer getTotalAvis() {
        return totalAvis;
    }

    public void setTotalAvis(Integer totalAvis) {
        this.totalAvis = totalAvis;
    }

    public String getUrlImagePrincipale() {
        return urlImagePrincipale;
    }

    public void setUrlImagePrincipale(String urlImagePrincipale) {
        this.urlImagePrincipale = urlImagePrincipale;
    }

    public List<String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }

    public Long getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(Long idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public String getProprietaireNom() {
        return proprietaireNom;
    }

    public void setProprietaireNom(String proprietaireNom) {
        this.proprietaireNom = proprietaireNom;
    }

    public String getProprietaireEntreprise() {
        return proprietaireEntreprise;
    }

    public void setProprietaireEntreprise(String proprietaireEntreprise) {
        this.proprietaireEntreprise = proprietaireEntreprise;
    }

    public List<EquipementDTO> getEquipements() {
        return equipements;
    }

    public void setEquipements(List<EquipementDTO> equipements) {
        this.equipements = equipements;
    }

    public LocalisationDTO getLocalisation() {
        return localisation;
    }

    public void setLocalisation(LocalisationDTO localisation) {
        this.localisation = localisation;
    }

    public List<DisponibiliteDTO> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(List<DisponibiliteDTO> disponibilites) {
        this.disponibilites = disponibilites;
    }

    public Double getPrixSurcharge() {
        return prixSurcharge;
    }

    public void setPrixSurcharge(Double prixSurcharge) {
        this.prixSurcharge = prixSurcharge;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    // Méthodes utilitaires


    /**
     * Vérifie si l'annonce a toutes les informations nécessaires
     */
    public boolean isValide() {
        return titre != null && !titre.trim().isEmpty() &&
                prix != null && prix > 0 &&
                adresse != null && !adresse.trim().isEmpty() &&
                ville != null && !ville.trim().isEmpty() &&
                nbreChambres != null && nbreChambres >= 0 &&
                maxInvites != null && maxInvites > 0 &&
                typeAnnonce != null && !typeAnnonce.trim().isEmpty() &&
                idProprietaire != null;
    }

    /**
     * Calcule le prix total avec surcharge
     */
    public Double getPrixTotal() {
        if (prix == null) return 0.0;
        if (prixSurcharge != null && prixSurcharge > 0) {
            return prix + prixSurcharge;
        }
        return prix;
    }

    /**
     * Met à jour l'évaluation moyenne
     */
    public void updateEvaluation(Double nouvelleNote) {
        if (nouvelleNote != null && nouvelleNote >= 0 && nouvelleNote <= 5) {
            Double totalNotes = (evaluationMoyenne != null ? evaluationMoyenne * totalAvis : 0);
            totalAvis = (totalAvis != null ? totalAvis : 0) + 1;
            evaluationMoyenne = (totalNotes + nouvelleNote) / totalAvis;
        }
    }

    @Override
    public String toString() {
        return "AnnonceDTO{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", prix=" + prix +
                ", ville='" + ville + '\'' +
                ", typeAnnonce='" + typeAnnonce + '\'' +
                ", estActive=" + estActive +
                ", evaluationMoyenne=" + evaluationMoyenne +
                '}';
    }
}