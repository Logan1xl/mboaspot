package com.example.backend.entities;

import com.example.backend.roles.RoleUtilisateur;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "voyageur", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Voyageur.findAll", query = "SELECT v FROM Voyageur v"),
    @NamedQuery(name = "Voyageur.findById", query = "SELECT v FROM Voyageur v WHERE v.id = :id")})
public class Voyageur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Lob
    @Column(name = "preferences")
    private String preferences;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVoyageur")
    private List<Avis> avisList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVoyageur")
    private List<Favori> favoriList;
    @OneToMany(mappedBy = "idVoyageur")
    private List<Reservation> reservationList;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur idUser;
    @Enumerated(EnumType.STRING)
    private RoleUtilisateur roleUtilisateur;

    public Voyageur() {
    }

    public Voyageur(Long id, List<Avis> avisList, String preferences, List<Favori> favoriList, List<Reservation> reservationList, Utilisateur idUser, RoleUtilisateur roleUtilisateur) {
        this.id = id;
        this.avisList = avisList;
        this.preferences = preferences;
        this.favoriList = favoriList;
        this.reservationList = reservationList;
        this.idUser = idUser;
        this.roleUtilisateur = roleUtilisateur;
    }

    public RoleUtilisateur getRoleUtilisateur() {
        return roleUtilisateur;
    }

    public void setRoleUtilisateur(RoleUtilisateur roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }

    public Voyageur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<Avis> getAvisList() {
        return avisList;
    }

    public void setAvisList(List<Avis> avisList) {
        this.avisList = avisList;
    }

    public List<Favori> getFavoriList() {
        return favoriList;
    }

    public void setFavoriList(List<Favori> favoriList) {
        this.favoriList = favoriList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Utilisateur getIdUser() {
        return idUser;
    }

    public void setIdUser(Utilisateur idUser) {
        this.idUser = idUser;
    }


    public Voyageur(Long id, String preferences, List<Avis> avisList, List<Favori> favoriList, List<Reservation> reservationList, Utilisateur idUser) {
        this.id = id;
        this.preferences = preferences;
        this.avisList = avisList;
        this.favoriList = favoriList;
        this.reservationList = reservationList;
        this.idUser = idUser;
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
        if (!(object instanceof Voyageur)) {
            return false;
        }
        Voyageur other = (Voyageur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Voyageur[ id=" + id + " ]";
    }

}
