package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "equipement", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Equipement.findAll", query = "SELECT e FROM Equipement e"),
    @NamedQuery(name = "Equipement.findById", query = "SELECT e FROM Equipement e WHERE e.id = :id"),
    @NamedQuery(name = "Equipement.findByNom", query = "SELECT e FROM Equipement e WHERE e.nom = :nom"),
    @NamedQuery(name = "Equipement.findByIcone", query = "SELECT e FROM Equipement e WHERE e.icone = :icone")})
public class Equipement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "icone")
    private String icone;
    @Lob
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "equipementList")
    private List<Annonces> annoncesList;

    public Equipement() {
    }

    public Equipement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Annonces> getAnnoncesList() {
        return annoncesList;
    }

    public void setAnnoncesList(List<Annonces> annoncesList) {
        this.annoncesList = annoncesList;
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
        if (!(object instanceof Equipement)) {
            return false;
        }
        Equipement other = (Equipement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Equipement[ id=" + id + " ]";
    }
    
}
