package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Date;


/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "disponibilite", catalog = "logement_cameroun")
@NamedQueries({
    @NamedQuery(name = "Disponibilite.findAll", query = "SELECT d FROM Disponibilite d"),
    @NamedQuery(name = "Disponibilite.findById", query = "SELECT d FROM Disponibilite d WHERE d.id = :id"),
    @NamedQuery(name = "Disponibilite.findByEstDisponible", query = "SELECT d FROM Disponibilite d WHERE d.estDisponible = :estDisponible"),
    @NamedQuery(name = "Disponibilite.findByPrixSurcharge", query = "SELECT d FROM Disponibilite d WHERE d.prixSurcharge = :prixSurcharge"),
    @NamedQuery(name = "Disponibilite.findByDateDebut", query = "SELECT d FROM Disponibilite d WHERE d.dateDebut = :dateDebut"),
    @NamedQuery(name = "Disponibilite.findByDateFin", query = "SELECT d FROM Disponibilite d WHERE d.dateFin = :dateFin")})
public class Disponibilite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "est_disponible")
    private Boolean estDisponible;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix_surcharge")
    private Double prixSurcharge;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @JoinColumn(name = "id_annonce", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces idAnnonce;

    public Disponibilite() {
    }

    public Disponibilite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(Boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public Double getPrixSurcharge() {
        return prixSurcharge;
    }

    public void setPrixSurcharge(Double prixSurcharge) {
        this.prixSurcharge = prixSurcharge;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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
        if (!(object instanceof Disponibilite)) {
            return false;
        }
        Disponibilite other = (Disponibilite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Disponibilite[ id=" + id + " ]";
    }
    
}
