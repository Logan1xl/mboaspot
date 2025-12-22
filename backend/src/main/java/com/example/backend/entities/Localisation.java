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
    @NamedQuery(name = "Localisation.findAll", query = "SELECT l FROM Localisation l"),
    @NamedQuery(name = "Localisation.findById", query = "SELECT l FROM Localisation l WHERE l.id = :id"),
    @NamedQuery(name = "Localisation.findByVille", query = "SELECT l FROM Localisation l WHERE l.ville = :ville"),
    @NamedQuery(name = "Localisation.findByQuartier", query = "SELECT l FROM Localisation l WHERE l.quartier = :quartier"),
    @NamedQuery(name = "Localisation.findByLatitude", query = "SELECT l FROM Localisation l WHERE l.latitude = :latitude"),
    @NamedQuery(name = "Localisation.findByLongitude", query = "SELECT l FROM Localisation l WHERE l.longitude = :longitude")})
public class Localisation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "ville")
    private String ville;
    @Column(name = "quartier")
    private String quartier;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @JoinColumn(name = "id_annonce", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces idAnnonce;

    public Localisation() {
    }

    public Localisation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
        if (!(object instanceof Localisation)) {
            return false;
        }
        Localisation other = (Localisation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Localisation[ id=" + id + " ]";
    }
    
}
