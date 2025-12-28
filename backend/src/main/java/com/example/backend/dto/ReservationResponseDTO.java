package com.example.backend.dto;

import java.time.LocalDate;

public class ReservationResponseDTO {
    private Long id;
    private String CodeConfirmation;
    private Integer nombreInvites;
    private Double prixTotal;
    private String statut;
    private Long annonceId;
    private String annonceTitre;
    private String annonceVille;
    private Long voyageurId;

    public ReservationResponseDTO(Long id, String codeConfirmation, Integer nombreInvites, Double prixTotal, String statut, Long annonceId, String annonceTitre, String annonceVille, Long voyageurId) {
        this.id = id;
        CodeConfirmation = codeConfirmation;
        this.nombreInvites = nombreInvites;
        this.prixTotal = prixTotal;
        this.statut = statut;
        this.annonceId = annonceId;
        this.annonceTitre = annonceTitre;
        this.annonceVille = annonceVille;
        this.voyageurId = voyageurId;
    }

    public ReservationResponseDTO() {
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

    public Long getVoyageurId() {
        return voyageurId;
    }

    public void setVoyageurId(Long voyageurId) {
        this.voyageurId = voyageurId;
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

}
//    private String voyageurTitre;

