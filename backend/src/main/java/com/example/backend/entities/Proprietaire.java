package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "proprietaire", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Proprietaire.findAll", query = "SELECT p FROM Proprietaire p"),
    @NamedQuery(name = "Proprietaire.findById", query = "SELECT p FROM Proprietaire p WHERE p.id = :id"),
    @NamedQuery(name = "Proprietaire.findByNomEntreprise", query = "SELECT p FROM Proprietaire p WHERE p.nomEntreprise = :nomEntreprise"),
    @NamedQuery(name = "Proprietaire.findByNumeroIdentification", query = "SELECT p FROM Proprietaire p WHERE p.numeroIdentification = :numeroIdentification"),
    @NamedQuery(name = "Proprietaire.findByCompteBancaire", query = "SELECT p FROM Proprietaire p WHERE p.compteBancaire = :compteBancaire"),
    @NamedQuery(name = "Proprietaire.findByGainsTotal", query = "SELECT p FROM Proprietaire p WHERE p.gainsTotal = :gainsTotal"),
    @NamedQuery(name = "Proprietaire.findByTotalAnnonces", query = "SELECT p FROM Proprietaire p WHERE p.totalAnnonces = :totalAnnonces"),
    @NamedQuery(name = "Proprietaire.findByEvaluationMoyenne", query = "SELECT p FROM Proprietaire p WHERE p.evaluationMoyenne = :evaluationMoyenne")})
public class Proprietaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nom_entreprise")
    private String nomEntreprise;
    @Column(name = "numero_identification")
    private String numeroIdentification;
    @Column(name = "compte_bancaire")
    private String compteBancaire;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "gains_total")
    private Double gainsTotal;
    @Column(name = "total_annonces")
    private Integer totalAnnonces;
    @Column(name = "evaluation_moyenne")
    private Double evaluationMoyenne;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur idUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProprietaire")
    private List<Annonces> annoncesList;

    public Proprietaire() {
    }

    public Proprietaire(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getCompteBancaire() {
        return compteBancaire;
    }

    public void setCompteBancaire(String compteBancaire) {
        this.compteBancaire = compteBancaire;
    }

    public Double getGainsTotal() {
        return gainsTotal;
    }

    public void setGainsTotal(Double gainsTotal) {
        this.gainsTotal = gainsTotal;
    }

    public Integer getTotalAnnonces() {
        return totalAnnonces;
    }

    public void setTotalAnnonces(Integer totalAnnonces) {
        this.totalAnnonces = totalAnnonces;
    }

    public Double getEvaluationMoyenne() {
        return evaluationMoyenne;
    }

    public void setEvaluationMoyenne(Double evaluationMoyenne) {
        this.evaluationMoyenne = evaluationMoyenne;
    }

    public Utilisateur getIdUser() {
        return idUser;
    }

    public void setIdUser(Utilisateur idUser) {
        this.idUser = idUser;
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
        if (!(object instanceof Proprietaire)) {
            return false;
        }
        Proprietaire other = (Proprietaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Proprietaire[ id=" + id + " ]";
    }
    
}
