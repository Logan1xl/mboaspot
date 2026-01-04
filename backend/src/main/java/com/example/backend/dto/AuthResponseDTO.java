package com.example.backend.dto;

import com.example.backend.roles.RoleUtilisateur;

public class AuthResponseDTO {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String nom;
    private String prenom;
    private RoleUtilisateur role;
    private String numeroTelephone;
    private String photoProfil;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, Long userId, String email, String nom,
                           String prenom, RoleUtilisateur role, String numeroTelephone,
                           String photoProfil) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.numeroTelephone = numeroTelephone;
        this.photoProfil = photoProfil;
    }

    // Getters et Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public RoleUtilisateur getRole() { return role; }
    public void setRole(RoleUtilisateur role) { this.role = role; }

    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }
}