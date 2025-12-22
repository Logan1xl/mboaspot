package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u"),
    @NamedQuery(name = "Utilisateur.findById", query = "SELECT u FROM Utilisateur u WHERE u.id = :id"),
    @NamedQuery(name = "Utilisateur.findByEmail", query = "SELECT u FROM Utilisateur u WHERE u.email = :email"),
    @NamedQuery(name = "Utilisateur.findByMotDePasse", query = "SELECT u FROM Utilisateur u WHERE u.motDePasse = :motDePasse"),
    @NamedQuery(name = "Utilisateur.findByPrenom", query = "SELECT u FROM Utilisateur u WHERE u.prenom = :prenom"),
    @NamedQuery(name = "Utilisateur.findByNom", query = "SELECT u FROM Utilisateur u WHERE u.nom = :nom"),
    @NamedQuery(name = "Utilisateur.findByNumeroTelephone", query = "SELECT u FROM Utilisateur u WHERE u.numeroTelephone = :numeroTelephone"),
    @NamedQuery(name = "Utilisateur.findByPhotoProfil", query = "SELECT u FROM Utilisateur u WHERE u.photoProfil = :photoProfil"),
    @NamedQuery(name = "Utilisateur.findByEstActif", query = "SELECT u FROM Utilisateur u WHERE u.estActif = :estActif"),
    @NamedQuery(name = "Utilisateur.findByRole", query = "SELECT u FROM Utilisateur u WHERE u.role = :role")})
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "mot_de_passe")
    private String motDePasse;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "nom")
    private String nom;
    @Column(name = "numero_telephone")
    private String numeroTelephone;
    @Column(name = "photo_profil")
    private String photoProfil;
    @Column(name = "est_actif")
    private Boolean estActif;
    @Basic(optional = false)
    @Column(name = "role")
    private String role;
    @OneToOne( mappedBy = "idUser")
    private Admin admin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private List<Notification> notificationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private List<Proprietaire> proprietaireList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private List<Voyageur> voyageurList;

    public Utilisateur() {
    }

    public Utilisateur(Long id) {
        this.id = id;
    }

    public Utilisateur(Long id, String email, String motDePasse, String role) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public List<Proprietaire> getProprietaireList() {
        return proprietaireList;
    }

    public void setProprietaireList(List<Proprietaire> proprietaireList) {
        this.proprietaireList = proprietaireList;
    }

    public List<Voyageur> getVoyageurList() {
        return voyageurList;
    }

    public void setVoyageurList(List<Voyageur> voyageurList) {
        this.voyageurList = voyageurList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Utilisateur[ id=" + id + " ]";
    }
    
}
