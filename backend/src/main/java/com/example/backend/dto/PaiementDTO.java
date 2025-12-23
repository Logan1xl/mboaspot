package com.example.backend.dto;

/**
 * DTO pour les paiements
 * @author Wulfrid MBONGO
 */
public class PaiementDTO {

    private Long id;
    private Double montant;
    private String idTransaction;
    private String urlRecepisse;
    private String methode;
    private String statut;
    private Long idReservation;

    // Informations additionnelles pour l'affichage
    private String codeConfirmationReservation;
    private String nomVoyageur;

    public PaiementDTO() {
    }

    public PaiementDTO(Long id, Double montant, String idTransaction,
                       String urlRecepisse, String methode, String statut,
                       Long idReservation) {
        this.id = id;
        this.montant = montant;
        this.idTransaction = idTransaction;
        this.urlRecepisse = urlRecepisse;
        this.methode = methode;
        this.statut = statut;
        this.idReservation = idReservation;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getUrlRecepisse() {
        return urlRecepisse;
    }

    public void setUrlRecepisse(String urlRecepisse) {
        this.urlRecepisse = urlRecepisse;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }

    public String getCodeConfirmationReservation() {
        return codeConfirmationReservation;
    }

    public void setCodeConfirmationReservation(String codeConfirmationReservation) {
        this.codeConfirmationReservation = codeConfirmationReservation;
    }

    public String getNomVoyageur() {
        return nomVoyageur;
    }

    public void setNomVoyageur(String nomVoyageur) {
        this.nomVoyageur = nomVoyageur;
    }

    @Override
    public String toString() {
        return "PaiementDTO{" +
                "id=" + id +
                ", montant=" + montant +
                ", idTransaction='" + idTransaction + '\'' +
                ", methode='" + methode + '\'' +
                ", statut='" + statut + '\'' +
                ", idReservation=" + idReservation +
                '}';
    }
}