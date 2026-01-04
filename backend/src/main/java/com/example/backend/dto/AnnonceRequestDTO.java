package com.example.backend.dto;

public class AnnonceRequestDTO {
    private String titre;
    private Double prix;
    private String adresse;

    private Double latitude;
    private Double longitude;
    private String quartier;
    private String ville;
    private Integer nbreChambres;
    private Integer nbreLits;
    private Integer maxInvites;
    private String description;
    private String typeAnnonce;
    private Long proprietaireId;

    // Constructeurs
    public AnnonceRequestDTO() {}

    public AnnonceRequestDTO(String titre, Double prix, String adresse, String ville,
                             Integer nbreChambres, Integer nbreLits, Integer maxInvites,
                             String description, String typeAnnonce, Long proprietaireId) {
        this.titre = titre;
        this.prix = prix;
        this.adresse = adresse;
        this.ville = ville;
        this.nbreChambres = nbreChambres;
        this.nbreLits = nbreLits;
        this.maxInvites = maxInvites;
        this.description = description;
        this.typeAnnonce = typeAnnonce;
        this.proprietaireId = proprietaireId;
    }

    // Getters et Setters
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

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
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

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }
}
