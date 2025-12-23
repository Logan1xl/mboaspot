package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "favori", catalog = "logement_cameroun")
@NamedQueries({
    @NamedQuery(name = "Favori.findAll", query = "SELECT f FROM Favori f"),
    @NamedQuery(name = "Favori.findById", query = "SELECT f FROM Favori f WHERE f.id = :id")})
public class Favori implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "id_voyageur", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Voyageur idVoyageur;
    @JoinColumn(name = "id_annonce", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces idAnnonce;

    public Favori() {
    }

    public Favori(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Voyageur getIdVoyageur() {
        return idVoyageur;
    }

    public void setIdVoyageur(Voyageur idVoyageur) {
        this.idVoyageur = idVoyageur;
    }

    public Annonces getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(Annonces idAnnonce) {
        this.idAnnonce = idAnnonce;
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
        if (!(object instanceof Favori)) {
            return false;
        }
        Favori other = (Favori) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Favori[ id=" + id + " ]";
    }
    
}
