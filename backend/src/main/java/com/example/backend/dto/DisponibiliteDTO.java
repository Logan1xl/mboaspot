package com.example.backend.dto;
import java.util.Date;

// ===== DisponibiliteDTO =====
public class DisponibiliteDTO {
    private Long id;
    private Boolean estDisponible;
    private Double prixSurcharge;
    private Date dateDebut;
    private Date dateFin;
    private Long idAnnonce;

    // Constructeurs
    public DisponibiliteDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Boolean getEstDisponible() { return estDisponible; }
    public void setEstDisponible(Boolean estDisponible) { this.estDisponible = estDisponible; }

    public Double getPrixSurcharge() { return prixSurcharge; }
    public void setPrixSurcharge(Double prixSurcharge) { this.prixSurcharge = prixSurcharge; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public Long getIdAnnonce() { return idAnnonce; }
    public void setIdAnnonce(Long idAnnonce) { this.idAnnonce = idAnnonce; }
}
