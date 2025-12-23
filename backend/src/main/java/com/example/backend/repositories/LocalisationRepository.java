package com.example.backend.repositories;

import com.example.backend.entities.Localisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalisationRepository extends JpaRepository<Localisation, Long> {

    // Trouver la localisation d'une annonce
    Optional<Localisation> findByIdAnnonceId(Long annonceId);

    // Trouver toutes les localisations dans une ville
    List<Localisation> findByVille(String ville);

    // Trouver par ville et quartier
    List<Localisation> findByVilleAndQuartier(String ville, String quartier);

    // Lister tous les quartiers d'une ville
    @Query("SELECT DISTINCT l.quartier FROM Localisation l WHERE l.ville = :ville")
    List<String> findQuartiersByVille(@Param("ville") String ville);

    // Lister toutes les villes
    @Query("SELECT DISTINCT l.ville FROM Localisation l")
    List<String> findAllVilles();
}
