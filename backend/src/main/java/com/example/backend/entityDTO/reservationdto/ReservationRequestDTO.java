package com.example.backend.entityDTO.reservationdto;

import java.time.LocalDate;

public class ReservationRequestDTO {
    private Long annoncesid;
    private String annonceTitre;
    private Long voyageurID;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreInvites;

    public ReservationRequestDTO(String annonceTitre) {
        this.annonceTitre = annonceTitre;
    }

    public ReservationRequestDTO(Long id, String annonceTitre, Long voyageurID, LocalDate dateDebut, LocalDate dateFin, Integer nombreInvites) {
        this.annoncesid = id;
        this.annonceTitre = annonceTitre;
        this.voyageurID = voyageurID;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreInvites = nombreInvites;
    }

    public Long getId() {
        return annoncesid;
    }

    public void setId(Long id) {
        this.annoncesid = id;
    }

    public Long getAnnoncesid() {
        return annoncesid;
    }

    public void setAnnoncesid(Long annoncesid) {
        this.annoncesid = annoncesid;
    }

    public Long getVoyageurID() {
        return voyageurID;
    }

    public void setVoyageurID(Long voyageurID) {
        this.voyageurID = voyageurID;
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

    public String getAnnonceTitre() {
        return annonceTitre;
    }

    public void setAnnonceTitre(String annonceTitre) {
        this.annonceTitre = annonceTitre;
    }
}
