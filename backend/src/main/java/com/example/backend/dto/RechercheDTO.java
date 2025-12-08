package com.example.backend.dto;

public class RechercheDTO {
    private String ville;
    private String typeAnnonce;
    private Double prixMin;
    private Double prixMax;
    private Integer nbreChambres;
    private Double evaluationMin;
    private Double latitude;
    private Double longitude;
    private Double rayon; // en km

    // Constructeurs
    public RechercheDTO() {}

    // Getters et Setters
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getTypeAnnonce() { return typeAnnonce; }
    public void setTypeAnnonce(String typeAnnonce) { this.typeAnnonce = typeAnnonce; }

    public Double getPrixMin() { return prixMin; }
    public void setPrixMin(Double prixMin) { this.prixMin = prixMin; }

    public Double getPrixMax() { return prixMax; }
    public void setPrixMax(Double prixMax) { this.prixMax = prixMax; }

    public Integer getNbreChambres() { return nbreChambres; }
    public void setNbreChambres(Integer nbreChambres) { this.nbreChambres = nbreChambres; }

    public Double getEvaluationMin() { return evaluationMin; }
    public void setEvaluationMin(Double evaluationMin) { this.evaluationMin = evaluationMin; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getRayon() { return rayon; }
    public void setRayon(Double rayon) { this.rayon = rayon; }
}