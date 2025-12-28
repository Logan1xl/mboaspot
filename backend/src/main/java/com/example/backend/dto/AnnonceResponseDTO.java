package com.example.backend.dto;
public class AnnonceResponseDTO {

    private Long id;
    private String titre;
    private Double prix;
    private String adresse;
    private String ville;
    private Integer nbreChambres;
    private Integer nbreLits;
    private Integer maxInvites;
    private String description;
    private String typeAnnonce;
    private Boolean estActive;
    private Double evaluationMoyenne;
    private Integer totalAvis;
    private String urlImagePrincipale;

    // Infos du propriétaire
    private Long proprietaireId;
    private String proprietaireNom;

    // Constructeur
    public AnnonceResponseDTO() {}

    // Getters et Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
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

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public String getProprietaireNom() {
        return proprietaireNom;
    }

    public void setProprietaireNom(String proprietaireNom) {
        this.proprietaireNom = proprietaireNom;
    }
}