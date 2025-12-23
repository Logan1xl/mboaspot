package com.example.backend.dto;

import java.time.LocalDate;

/**
 * DTO pour simuler une réservation (mock)
 * @author Lumiere NGO NGWA
 */
public class ReservationDTO {
    
    private Long id;
    private Long idUtilisateur;
    private String nomUtilisateur;
    private String nomLogement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double montant;
    private String statut;
    
    public ReservationDTO() {
    }
    
    public ReservationDTO(Long id, Long idUtilisateur, String nomUtilisateur, 
                         String nomLogement, LocalDate dateDebut, LocalDate dateFin, 
                         Double montant, String statut) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.nomLogement = nomLogement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getNomLogement() {
        return nomLogement;
    }

    public void setNomLogement(String nomLogement) {
        this.nomLogement = nomLogement;
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

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
