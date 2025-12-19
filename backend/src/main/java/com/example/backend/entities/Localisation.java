package com.example.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entité représentant la localisation d'une annonce,
 * avec les NamedQueries d'origine réintégrés.
 */
@Entity
@Table(name = "localisation", catalog = "logement_cameroun", schema = "")
@NamedQueries({
        @NamedQuery(name = "Localisation.findAll", query = "SELECT l FROM Localisation l"),
        @NamedQuery(name = "Localisation.findById", query = "SELECT l FROM Localisation l WHERE l.id = :id"),
        @NamedQuery(name = "Localisation.findByVille", query = "SELECT l FROM Localisation l WHERE l.ville = :ville"),
        @NamedQuery(name = "Localisation.findByQuartier", query = "SELECT l FROM Localisation l WHERE l.quartier = :quartier"),
        @NamedQuery(name = "Localisation.findByLatitude", query = "SELECT l FROM Localisation l WHERE l.latitude = :latitude"),
        @NamedQuery(name = "Localisation.findByLongitude", query = "SELECT l FROM Localisation l WHERE l.longitude = :longitude")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Localisation implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Clé primaire de la table */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;

    /** Ville où se trouve l’annonce */
    private String ville;

    /** Quartier géographique */
    private String quartier;

    /** Coordonnée GPS : latitude */
    private Double latitude;

    /** Coordonnée GPS : longitude */
    private Double longitude;

    /**
     * Relation ManyToOne avec Annonces.
     * Une annonce peut avoir plusieurs localisations.
     */
    @JoinColumn(name = "id_annonce", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Annonces idAnnonce;
}
