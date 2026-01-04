package com.example.backend.repositories;

import com.example.backend.entities.Annonces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnoncesRepository extends JpaRepository<Annonces, Long> {

    // Recherche par ville
    List<Annonces> findByVilleAndEstActiveTrue(String ville);

    // Recherche par type d'annonce
    List<Annonces> findByTypeAnnonceAndEstActiveTrue(String typeAnnonce);

    // Recherche par propriétaire
    List<Annonces> findByIdProprietaireId(Long proprietaireId);

    // Recherche par prix
    List<Annonces> findByPrixBetweenAndEstActiveTrue(Double prixMin, Double prixMax);

    // Recherche avancée avec plusieurs critères
    @Query("SELECT a FROM Annonces a WHERE " +
            "(:ville IS NULL OR a.ville = :ville) AND " +
            "(:typeAnnonce IS NULL OR a.typeAnnonce = :typeAnnonce) AND " +
            "(:prixMin IS NULL OR a.prix >= :prixMin) AND " +
            "(:prixMax IS NULL OR a.prix <= :prixMax) AND " +
            "(:nbreChambres IS NULL OR a.nbreChambres >= :nbreChambres) AND " +
            "(:evaluationMin IS NULL OR a.evaluationMoyenne >= :evaluationMin) AND " +
            "a.estActive = true")
    List<Annonces> rechercheAvancee(
            @Param("ville") String ville,
            @Param("typeAnnonce") String typeAnnonce,
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax,
            @Param("nbreChambres") Integer nbreChambres,
            @Param("evaluationMin") Double evaluationMin
    );

    // Top annonces par évaluation
    List<Annonces> findTop10ByEstActiveTrueOrderByEvaluationMoyenneDesc();

    // Annonces proches d'une position (géolocalisation)
    @Query("SELECT a FROM Annonces a WHERE a.estActive = true AND " +
            "((6371 * acos(cos(radians(:latitude)) * cos(radians(a.latitude)) * " +
            "cos(radians(a.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(a.latitude)))) < :rayon)")
    List<Annonces> findAnnoncesProches(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("rayon") Double rayon
    );

    // Compter les annonces par ville
    @Query("SELECT a.ville, COUNT(a) FROM Annonces a WHERE a.estActive = true GROUP BY a.ville")
    List<Object[]> countAnnoncesByVille();
    Annonces findAnnoncesById(Long id);

    // Trouver les annonces actives
    List<Annonces> findByEstActive(Boolean estActive);

    // Trouver les annonces d'un propriétaire
    List<Annonces> findByIdProprietaire_Id(Long proprietaireId);

    // Trouver les annonces par ville
    List<Annonces> findByVille(String ville);

    // Rechercher les annonces actives par ville
    @Query("SELECT a FROM Annonces a WHERE a.ville = :ville AND a.estActive = true")
    List<Annonces> findAnnoncesActivesByVille(@Param("ville") String ville);

    // Rechercher les annonces avec un prix dans une fourchette
    @Query("SELECT a FROM Annonces a WHERE a.prix >= :prixMin AND a.prix <= :prixMax AND a.estActive = true")
    List<Annonces> findByPrixBetween(
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax
    );
}