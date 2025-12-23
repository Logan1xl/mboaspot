package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "avis", catalog = "logement_cameroun")
@NamedQueries({
    @NamedQuery(name = "Avis.findAll", query = "SELECT a FROM Avis a"),
    @NamedQuery(name = "Avis.findById", query = "SELECT a FROM Avis a WHERE a.id = :id"),
    @NamedQuery(name = "Avis.findByNote", query = "SELECT a FROM Avis a WHERE a.note = :note"),
    @NamedQuery(name = "Avis.findByEstSignale", query = "SELECT a FROM Avis a WHERE a.estSignale = :estSignale")})
public class Avis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "note")
    private Integer note;
    @Lob
    @Column(name = "photos")
    private String photos;
    @Column(name = "est_signale")
    private Boolean estSignale;
    @Lob
    @Column(name = "raison_signalement")
    private String raisonSignalement;
    @JoinColumn(name = "id_voyageur", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Voyageur idVoyageur;

    public Avis() {
    }

    public Avis(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Boolean getEstSignale() {
        return estSignale;
    }

    public void setEstSignale(Boolean estSignale) {
        this.estSignale = estSignale;
    }

    public String getRaisonSignalement() {
        return raisonSignalement;
    }

    public void setRaisonSignalement(String raisonSignalement) {
        this.raisonSignalement = raisonSignalement;
    }

    public Voyageur getIdVoyageur() {
        return idVoyageur;
    }

    public void setIdVoyageur(Voyageur idVoyageur) {
        this.idVoyageur = idVoyageur;
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
        if (!(object instanceof Avis)) {
            return false;
        }
        Avis other = (Avis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Avis[ id=" + id + " ]";
    }
    
}
