package com.example.backend.dto;

public class LocalisationDTO {
    private Long id;
    private String ville;
    private String quartier;
    private Double latitude;
    private Double longitude;
    private Long idAnnonce;

    // Constructeurs
    public LocalisationDTO() {}

    public LocalisationDTO(Long id, String ville, String quartier, Double latitude, Double longitude, Long idAnnonce) {
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

    public Long getIdAnnonce() { return idAnnonce; }
    public void setIdAnnonce(Long idAnnonce) { this.idAnnonce = idAnnonce; }
}
import com.example.backend.entities.Annonces;
import org.hibernate.validator.constraints.Length;

import com.example.backend.entities.Localisation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalisationDTO {
    private Long id;
   
    private String ville;

   
    private String quartier;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Length(max = 50)
    private String latitude;
   @Length(max = 50)
    private String longitude;
   
    private Annonces idAnnonce;


    public void setNom(String zoneB) {
    }

    public String getNom() {
        return null;
    }
}
