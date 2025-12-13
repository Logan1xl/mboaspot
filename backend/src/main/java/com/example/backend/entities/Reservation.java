package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "reservation", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Reservation.findAll", query = "SELECT r FROM Reservation r"),
    @NamedQuery(name = "Reservation.findById", query = "SELECT r FROM Reservation r WHERE r.id = :id"),
    @NamedQuery(name = "Reservation.findByNombreInvites", query = "SELECT r FROM Reservation r WHERE r.nombreInvites = :nombreInvites"),
    @NamedQuery(name = "Reservation.findByPrixTotal", query = "SELECT r FROM Reservation r WHERE r.prixTotal = :prixTotal"),
    @NamedQuery(name = "Reservation.findByCodeConfirmation", query = "SELECT r FROM Reservation r WHERE r.codeConfirmation = :codeConfirmation"),
    @NamedQuery(name = "Reservation.findByStatut", query = "SELECT r FROM Reservation r WHERE r.statut = :statut")})
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre_invites")
    private Integer nombreInvites;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix_total")
    private Double prixTotal;
    @Column(name = "code_confirmation")
    private String codeConfirmation;
    @Column(name = "statut")
    private String statut;
    @OneToMany(mappedBy = "idReservation")
    private List<Paiement> paiementList;
    @JoinColumn(name = "id_voyageur", referencedColumnName = "id")
    @ManyToOne
    private Voyageur idVoyageur;
    @JoinColumn(name="id_annonces",referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces annonce;
    @Column(name = "date_debut",nullable = false)
    private Date dateDebut;
    @Column(name = "date_fin", nullable = false)
    private Date dateFin;

    public Reservation(Date dateFin, Date dateDebut, Annonces annonce) {
        this.dateFin = dateFin;
        this.dateDebut = dateDebut;
        this.annonce = annonce;
    }

    public Annonces getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonces annonce) {
        this.annonce = annonce;
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

    public Reservation() {
    }

    public Reservation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNombreInvites() {
        return nombreInvites;
    }

    public void setNombreInvites(Integer nombreInvites) {
        this.nombreInvites = nombreInvites;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getCodeConfirmation() {
        return codeConfirmation;
    }

    public void setCodeConfirmation(String codeConfirmation) {
        this.codeConfirmation = codeConfirmation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<Paiement> getPaiementList() {
        return paiementList;
    }

    public void setPaiementList(List<Paiement> paiementList) {
        this.paiementList = paiementList;
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
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Reservation[ id=" + id + " ]";
    }
    
}
