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
    private Integer nbreChambres;
    private Integer nbreLits;
    private Integer maxInvites;
    private String description;
    private Boolean estActive;
    private Double evaluationMoyenne;
    private Integer totalAvis;
    private String urlImagePrincipale;
    private List<String> urlImages;
    private String typeAnnonce;
    private Long idProprietaire;
    private String nomProprietaire;
    private List<EquipementDTO> equipements;
    private LocalisationDTO localisation;

    // Constructeurs
    public AnnonceDTO() {}

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

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public Long getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(Long idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
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
}
