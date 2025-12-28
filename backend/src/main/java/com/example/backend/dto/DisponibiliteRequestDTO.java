package com.example.backend.dto;

import java.time.LocalDate;

public class DisponibiliteRequestDTO {

    private Long annonceId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreInvites;

    public DisponibiliteRequestDTO(Long annonceId, LocalDate dateDebut, LocalDate dateFin, Integer nombreInvites) {
        this.annonceId = annonceId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreInvites = nombreInvites;
    }

    public DisponibiliteRequestDTO() {
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getNombreInvites() {
        return nombreInvites;
    }

    public void setNombreInvites(Integer nombreInvites) {
        this.nombreInvites = nombreInvites;
    }
}
