package com.example.backend.dto;

import com.example.backend.entities.Annonces;

public class LocalisationDTO {
    private Long id;
    private String ville;
    private String quartier;
    private Double latitude;
    private Double longitude;
    private Annonces idAnnonce;

    // Constructeurs
    public LocalisationDTO() {}

    public LocalisationDTO(Long id, String ville, String quartier, Double latitude, Double longitude, Annonces idAnnonce) {
        this.id = id;
        this.ville = ville;
        this.quartier = quartier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idAnnonce = idAnnonce;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getQuartier() { return quartier; }
    public void setQuartier(String quartier) { this.quartier = quartier; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Annonces getIdAnnonce() { return idAnnonce; }
    public void setIdAnnonce(Annonces idAnnonce) { this.idAnnonce = idAnnonce; }
}