package com.example.backend.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 *
 * @author Wulfrid MBONGO
 */
@Entity
@Table(name = "annonces", catalog = "logement_cameroun", schema = "")
@NamedQueries({
    @NamedQuery(name = "Annonces.findAll", query = "SELECT a FROM Annonces a"),
    @NamedQuery(name = "Annonces.findById", query = "SELECT a FROM Annonces a WHERE a.id = :id"),
    @NamedQuery(name = "Annonces.findByTitre", query = "SELECT a FROM Annonces a WHERE a.titre = :titre"),
    @NamedQuery(name = "Annonces.findByPrix", query = "SELECT a FROM Annonces a WHERE a.prix = :prix"),
    @NamedQuery(name = "Annonces.findByAdresse", query = "SELECT a FROM Annonces a WHERE a.adresse = :adresse"),
    @NamedQuery(name = "Annonces.findByLatitude", query = "SELECT a FROM Annonces a WHERE a.latitude = :latitude"),
    @NamedQuery(name = "Annonces.findByLongitude", query = "SELECT a FROM Annonces a WHERE a.longitude = :longitude"),
    @NamedQuery(name = "Annonces.findByVille", query = "SELECT a FROM Annonces a WHERE a.ville = :ville"),
    @NamedQuery(name = "Annonces.findByNbreChambres", query = "SELECT a FROM Annonces a WHERE a.nbreChambres = :nbreChambres"),
    @NamedQuery(name = "Annonces.findByNbreLits", query = "SELECT a FROM Annonces a WHERE a.nbreLits = :nbreLits"),
    @NamedQuery(name = "Annonces.findByMaxInvites", query = "SELECT a FROM Annonces a WHERE a.maxInvites = :maxInvites"),
    @NamedQuery(name = "Annonces.findByEstActive", query = "SELECT a FROM Annonces a WHERE a.estActive = :estActive"),
    @NamedQuery(name = "Annonces.findByEvaluationMoyenne", query = "SELECT a FROM Annonces a WHERE a.evaluationMoyenne = :evaluationMoyenne"),
    @NamedQuery(name = "Annonces.findByTotalAvis", query = "SELECT a FROM Annonces a WHERE a.totalAvis = :totalAvis"),
    @NamedQuery(name = "Annonces.findByUrlImagePrincipale", query = "SELECT a FROM Annonces a WHERE a.urlImagePrincipale = :urlImagePrincipale"),
    @NamedQuery(name = "Annonces.findByTypeAnnonce", query = "SELECT a FROM Annonces a WHERE a.typeAnnonce = :typeAnnonce")})
public class Annonces implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "titre")
    private String titre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "ville")
    private String ville;
    @Column(name = "nbre_chambres")
    private Integer nbreChambres;
    @Column(name = "nbre_lits")
    private Integer nbreLits;
    @Column(name = "max_invites")
    private Integer maxInvites;
    @Lob
    @Column(name = "url_images")
    private String urlImages;
    @Column(name = "est_active")
    private Boolean estActive;
    @Column(name = "evaluation_moyenne")
    private Double evaluationMoyenne;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "total_avis")
    private Integer totalAvis;
    @Column(name = "url_image_principale")
    private String urlImagePrincipale;
    @Column(name = "type_annonce")
    private String typeAnnonce;
    @JoinTable(name = "annonce_equipement", joinColumns = {
        @JoinColumn(name = "id_annonce", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_equipement", referencedColumnName = "id")})
    @ManyToMany
    private List<Equipement> equipementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAnnonce")
    private List<Disponibilite> disponibiliteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAnnonce")
    private List<Localisation> localisationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAnnonce")
    private List<Signalement> signalementList;
    @JoinColumn(name = "id_proprietaire", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proprietaire idProprietaire;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAnnonce")
    private List<Favori> favoriList;

    public Annonces() {
    }

    public Annonces(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getNbreChambres() {
        return nbreChambres;
    }

    public void setNbreChambres(Integer nbreChambres) {
        this.nbreChambres = nbreChambres;
    }

    public Integer getNbreLits() {
        return nbreLits;
    }

    public void setNbreLits(Integer nbreLits) {
        this.nbreLits = nbreLits;
    }

    public Integer getMaxInvites() {
        return maxInvites;
    }

    public void setMaxInvites(Integer maxInvites) {
        this.maxInvites = maxInvites;
    }

    public String getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(String urlImages) {
        this.urlImages = urlImages;
    }

    public Boolean getEstActive() {
        return estActive;
    }

    public void setEstActive(Boolean estActive) {
        this.estActive = estActive;
    }

    public Double getEvaluationMoyenne() {
        return evaluationMoyenne;
    }

    public void setEvaluationMoyenne(Double evaluationMoyenne) {
        this.evaluationMoyenne = evaluationMoyenne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalAvis() {
        return totalAvis;
    }

    public void setTotalAvis(Integer totalAvis) {
        this.totalAvis = totalAvis;
    }

    public String getUrlImagePrincipale() {
        return urlImagePrincipale;
    }

    public void setUrlImagePrincipale(String urlImagePrincipale) {
        this.urlImagePrincipale = urlImagePrincipale;
    }

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }

    public List<Equipement> getEquipementList() {
        return equipementList;
    }

    public void setEquipementList(List<Equipement> equipementList) {
        this.equipementList = equipementList;
    }

    public List<Disponibilite> getDisponibiliteList() {
        return disponibiliteList;
    }

    public void setDisponibiliteList(List<Disponibilite> disponibiliteList) {
        this.disponibiliteList = disponibiliteList;
    }

    public List<Localisation> getLocalisationList() {
        return localisationList;
    }

    public void setLocalisationList(List<Localisation> localisationList) {
        this.localisationList = localisationList;
    }

    public List<Signalement> getSignalementList() {
        return signalementList;
    }

    public void setSignalementList(List<Signalement> signalementList) {
        this.signalementList = signalementList;
    }

    public Proprietaire getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(Proprietaire idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public List<Favori> getFavoriList() {
        return favoriList;
    }

    public void setFavoriList(List<Favori> favoriList) {
        this.favoriList = favoriList;
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
        if (!(object instanceof Annonces)) {
            return false;
        }
        Annonces other = (Annonces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Annonces[ id=" + id + " ]";
    }
    
}
