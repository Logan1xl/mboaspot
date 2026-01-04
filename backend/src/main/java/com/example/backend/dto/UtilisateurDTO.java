package com.example.backend.dto;

import com.example.backend.roles.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class UtilisateurDTO {
    private Long id;
    private String email;

    public UtilisateurDTO() {
    }


    private String motDePasse;
    private String prenom;
    private String nom;

    private String numeroTelephone;
    private String photoProfil;
    private Boolean estActif;
    private RoleUtilisateur role;

    public UtilisateurDTO(Long id, String email, String motDePasse, String prenom, String nom, String numeroTelephone, String photoProfil, Boolean estActif, RoleUtilisateur role) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.nom = nom;
        this.numeroTelephone = numeroTelephone;
        this.photoProfil = photoProfil;
        this.estActif = estActif;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }


}
