package com.example.backend.entityDTO.reservationdto;

import java.time.LocalDate;

public class ReservationResponseDTO {
    private Long id;
    private String CodeConfirmation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreInvites;
    private Double prixTotal;
    private String statut;
    private Long annonceId;
    private String annonceTitre;
    private String annonceVille;
    private Long voyageurId;
    private String voyageurTitre;

    public ReservationResponseDTO() {
    }

    public ReservationResponseDTO(Long id, String codeConfirmation, LocalDate dateDebut, LocalDate dateFin, Integer nombreInvites, Double prixTotal, String statut, Long annonceId, String anoonceTitre, String annonceVille, Long voyageurId, String voyageurTitre) {
        this.id = id;
        CodeConfirmation = codeConfirmation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreInvites = nombreInvites;
        this.prixTotal = prixTotal;
        this.statut = statut;
        this.annonceId = annonceId;
        this.annonceTitre = anoonceTitre;
        this.annonceVille = annonceVille;
        this.voyageurId = voyageurId;
        this.voyageurTitre = voyageurTitre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeConfirmation() {
        return CodeConfirmation;
    }

    public void setCodeConfirmation(String codeConfirmation) {
        CodeConfirmation = codeConfirmation;
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

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }

    public String getAnnonceTitre() {
        return annonceTitre;
    }

    public void setAnnonceTitre(String annonceTitre) {
        this.annonceTitre = annonceTitre;
    }

    public String getAnnonceVille() {
        return annonceVille;
    }

    public void setAnnonceVille(String annonceVille) {
        this.annonceVille = annonceVille;
    }

    public Long getVoyageurId() {
        return voyageurId;
    }

    public void setVoyageurId(Long voyageurId) {
        this.voyageurId = voyageurId;
    }

    public String getVoyageurTitre() {
        return voyageurTitre;
    }

    public void setVoyageurTitre(String voyageurTitre) {
        this.voyageurTitre = voyageurTitre;
    }
}
