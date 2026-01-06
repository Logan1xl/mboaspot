package com.example.backend.dto;

public class RechercheDTO {
    private String ville;
    private String quartier;
    private String typeAnnonce;
    private Double prixMin;
    private Double prixMax;
    private Integer nbreChambres;
    private Integer nbreChambresMin;
    private Integer nbreLits;
    private Integer nbreLitsMin;
    private Integer maxInvites;
    private Integer maxInvitesMin;
    private Double evaluationMin;
    private Double latitude;
    private Double longitude;
    private Double rayon; // en km
    private Double surfaceMin;

    // Constructeurs
    public RechercheDTO() {}

    public RechercheDTO(String ville, String typeAnnonce, Double prixMin, Double prixMax) {
        this.ville = ville;
        this.typeAnnonce = typeAnnonce;
        this.prixMin = prixMin;
        this.prixMax = prixMax;
    }

    // Getters et Setters
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

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public Double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Double getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(Double prixMax) {
        this.prixMax = prixMax;
    }

    public Integer getNbreChambres() {
        return nbreChambres;
    }

    public void setNbreChambres(Integer nbreChambres) {
        this.nbreChambres = nbreChambres;
    }

    public Integer getNbreChambresMin() {
        return nbreChambresMin;
    }

    public void setNbreChambresMin(Integer nbreChambresMin) {
        this.nbreChambresMin = nbreChambresMin;
    }

    public Integer getNbreLits() {
        return nbreLits;
    }

    public void setNbreLits(Integer nbreLits) {
        this.nbreLits = nbreLits;
    }

    public Integer getNbreLitsMin() {
        return nbreLitsMin;
    }

    public void setNbreLitsMin(Integer nbreLitsMin) {
        this.nbreLitsMin = nbreLitsMin;
    }

    public Integer getMaxInvites() {
        return maxInvites;
    }

    public void setMaxInvites(Integer maxInvites) {
        this.maxInvites = maxInvites;
    }

    public Integer getMaxInvitesMin() {
        return maxInvitesMin;
    }

    public void setMaxInvitesMin(Integer maxInvitesMin) {
        this.maxInvitesMin = maxInvitesMin;
    }

    public Double getEvaluationMin() {
        return evaluationMin;
    }

    public void setEvaluationMin(Double evaluationMin) {
        this.evaluationMin = evaluationMin;
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

    public Double getRayon() {
        return rayon;
    }

    public void setRayon(Double rayon) {
        this.rayon = rayon;
    }

    public Double getSurfaceMin() {
        return surfaceMin;
    }

    public void setSurfaceMin(Double surfaceMin) {
        this.surfaceMin = surfaceMin;
    }

    @Override
    public String toString() {
        return "RechercheDTO{" +
                "ville='" + ville + '\'' +
                ", quartier='" + quartier + '\'' +
                ", typeAnnonce='" + typeAnnonce + '\'' +
                ", prixMin=" + prixMin +
                ", prixMax=" + prixMax +
                ", nbreChambresMin=" + nbreChambresMin +
                ", nbreLitsMin=" + nbreLitsMin +
                ", maxInvitesMin=" + maxInvitesMin +
                ", evaluationMin=" + evaluationMin +
                '}';
    }
}