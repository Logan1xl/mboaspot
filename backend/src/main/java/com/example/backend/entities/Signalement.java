package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "signalement", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Signalement.findAll", query = "SELECT s FROM Signalement s"),
    @NamedQuery(name = "Signalement.findById", query = "SELECT s FROM Signalement s WHERE s.id = :id"),
    @NamedQuery(name = "Signalement.findByRaison", query = "SELECT s FROM Signalement s WHERE s.raison = :raison"),
    @NamedQuery(name = "Signalement.findByStatut", query = "SELECT s FROM Signalement s WHERE s.statut = :statut")})
public class Signalement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "raison")
    private String raison;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "resolution")
    private String resolution;
    @Column(name = "statut")
    private String statut;
    @JoinColumn(name = "id_annonce", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces idAnnonce;
    @JoinColumn(name = "id_admin", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Admin idAdmin;

    public Signalement() {
    }

    public Signalement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Annonces getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(Annonces idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public Admin getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Admin idAdmin) {
        this.idAdmin = idAdmin;
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
        if (!(object instanceof Signalement)) {
            return false;
        }
        Signalement other = (Signalement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Signalement[ id=" + id + " ]";
    }
    
}
