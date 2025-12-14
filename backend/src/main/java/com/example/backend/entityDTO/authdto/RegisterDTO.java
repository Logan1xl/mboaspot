package com.example.backend.entityDTO.authdto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Minimum 8 caractères")
    private String motDePasse;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String numeroTelephone;

    @NotBlank(message = "Le rôle est obligatoire")
    @Pattern(regexp = "ADMIN|PROPRIETAIRE|VOYAGEUR", message = "Rôle invalide")
    private String role;

    private String photoProfil;
    private String nomEntreprise;
    private String numeroIdentification;
    private String compteBancaire;
    private String preferences;
    private String departement;

    public RegisterDTO() {}

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }

    public String getNomEntreprise() { return nomEntreprise; }
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getNumeroIdentification() { return numeroIdentification; }
    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getCompteBancaire() { return compteBancaire; }
    public void setCompteBancaire(String compteBancaire) {
        this.compteBancaire = compteBancaire;
    }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
}