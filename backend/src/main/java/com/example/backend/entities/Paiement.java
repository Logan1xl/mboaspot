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
    @NamedQuery(name = "Paiement.findAll", query = "SELECT p FROM Paiement p"),
    @NamedQuery(name = "Paiement.findById", query = "SELECT p FROM Paiement p WHERE p.id = :id"),
    @NamedQuery(name = "Paiement.findByMontant", query = "SELECT p FROM Paiement p WHERE p.montant = :montant"),
    @NamedQuery(name = "Paiement.findByIdTransaction", query = "SELECT p FROM Paiement p WHERE p.idTransaction = :idTransaction"),
    @NamedQuery(name = "Paiement.findByUrlRecepisse", query = "SELECT p FROM Paiement p WHERE p.urlRecepisse = :urlRecepisse"),
    @NamedQuery(name = "Paiement.findByMethode", query = "SELECT p FROM Paiement p WHERE p.methode = :methode"),
    @NamedQuery(name = "Paiement.findByStatut", query = "SELECT p FROM Paiement p WHERE p.statut = :statut")})
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "id_transaction")
    private String idTransaction;
    @Column(name = "url_recepisse")
    private String urlRecepisse;
    @Column(name = "methode")
    private String methode;
    @Column(name = "statut")
    private String statut;
    @JoinColumn(name = "id_reservation", referencedColumnName = "id")
    @ManyToOne
    private Reservation idReservation;

    public Paiement() {
    }

    public Paiement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getUrlRecepisse() {
        return urlRecepisse;
    }

    public void setUrlRecepisse(String urlRecepisse) {
        this.urlRecepisse = urlRecepisse;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Reservation getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Reservation idReservation) {
        this.idReservation = idReservation;
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
        if (!(object instanceof Paiement)) {
            return false;
        }
        Paiement other = (Paiement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Paiement[ id=" + id + " ]";
    }
    
}
