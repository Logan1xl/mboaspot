package com.example.backend.repositories;

// Import de Spring Data JPA pour fournir les méthodes CRUD de base
import org.springframework.data.jpa.repository.JpaRepository;
// Import pour utiliser des requêtes JPQL personnalisées
// Annotation pour indiquer que c'est un composant Spring
import org.springframework.stereotype.Repository;

import com.example.backend.entities.Annonces;
import com.example.backend.entities.Localisation;

import java.util.List;

/**
 * Repository permettant d'interagir avec la table "localisation".
 * Hérite de JpaRepository pour fournir :
 *  - findAll()
 *  - findById()
 *  - save()
 *  - deleteById()
 *  - etc.
 */
@Repository
public interface LocalisationRepository extends JpaRepository<Localisation, Long> {



    /**
     * Recherche en fonction de la ville.
     * Spring Data JPA génère automatiquement le JPQL :
     * SELECT l FROM Localisation l WHERE l.ville = :ville
     */
    List<Localisation> findByVille(String ville);

    /**
     * Recherche en fonction du quartier.
     */
    List<Localisation> findByQuartier(String quartier);

    /**
     * Recherche en fonction de la latitude.
     */
    List<Localisation> findByLatitude(Double latitude);

    /**
     * Recherche en fonction de la longitude.
     */
    List<Localisation> findByLongitude(Double longitude);

    /**
     * Recherche toutes les localisations liées à une annonce donnée.
     * Spring Data interprète "AnnoncesId" comme :
     * SELECT l FROM Localisation l WHERE l.idAnnonce.id = :annoncesId
     */
    // List<Localisation> findByAnnonceId(Long annoncesId);

    List<Localisation> findByIdAnnonce(Annonces idAnnonce);
}
