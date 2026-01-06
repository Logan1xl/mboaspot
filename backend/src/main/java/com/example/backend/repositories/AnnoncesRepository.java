package com.example.backend.repositories;

import com.example.backend.entities.Annonces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnoncesRepository extends JpaRepository<Annonces, Long> {

    // ===== Recherches de base =====

    // Trouver les annonces actives
    List<Annonces> findByEstActive(Boolean estActive);

    // Recherche par ville
    List<Annonces> findByVilleAndEstActiveTrue(String ville);

    // Recherche par type d'annonce
    List<Annonces> findByTypeAnnonceAndEstActiveTrue(String typeAnnonce);

    // Recherche par propriétaire
    List<Annonces> findByIdProprietaireId(Long proprietaireId);
    List<Annonces> findByIdProprietaire_Id(Long proprietaireId);

    // Recherche par ville
    List<Annonces> findByVille(String ville);

    // Trouver une annonce par ID (alternative)
    Annonces findAnnoncesById(Long id);

    // ===== Recherches avec prix =====

    // Recherche par prix
    List<Annonces> findByPrixBetweenAndEstActiveTrue(Double prixMin, Double prixMax);

    // Rechercher les annonces avec un prix dans une fourchette
    @Query("SELECT a FROM Annonces a WHERE a.prix >= :prixMin AND a.prix <= :prixMax AND a.estActive = true")
    List<Annonces> findByPrixBetween(
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax
    );

    // ===== Recherche avancée complète =====

    @Query("SELECT a FROM Annonces a WHERE " +
            "(:ville IS NULL OR a.ville = :ville) AND " +
            "(:quartier IS NULL OR a.ville = :quartier) AND " +
            "(:typeAnnonce IS NULL OR a.typeAnnonce = :typeAnnonce) AND " +
            "(:prixMin IS NULL OR a.prix >= :prixMin) AND " +
            "(:prixMax IS NULL OR a.prix <= :prixMax) AND " +
            "(:nbreChambresMin IS NULL OR a.nbreChambres >= :nbreChambresMin) AND " +
            "(:nbreLitsMin IS NULL OR a.nbreLits >= :nbreLitsMin) AND " +
            "(:maxInvitesMin IS NULL OR a.maxInvites >= :maxInvitesMin) AND " +
            "(:evaluationMin IS NULL OR a.evaluationMoyenne >= :evaluationMin) AND " +
            "a.estActive = true")
    List<Annonces> rechercheAvanceeComplete(
            @Param("ville") String ville,
            @Param("quartier") String quartier,
            @Param("typeAnnonce") String typeAnnonce,
            @Param("prixMin") Double prixMin,
            @Param("prixMax") Double prixMax,
            @Param("nbreChambresMin") Integer nbreChambresMin,
            @Param("nbreLitsMin") Integer nbreLitsMin,
            @Param("maxInvitesMin") Integer maxInvitesMin,
            @Param("evaluationMin") Double evaluationMin
    );

    // Recherche avancée (ancienne version pour compatibilité)
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

    // ===== Top annonces et statistiques =====

    // Top annonces par évaluation
    List<Annonces> findTop10ByEstActiveTrueOrderByEvaluationMoyenneDesc();

    // Top N annonces
    @Query("SELECT a FROM Annonces a WHERE a.estActive = true ORDER BY a.evaluationMoyenne DESC")
    List<Annonces> findTopAnnonces();

    // Compter les annonces actives
    @Query("SELECT COUNT(a) FROM Annonces a WHERE a.estActive = true")
    Long countAnnoncesActives();

    // Compter les annonces par ville
    @Query("SELECT a.ville, COUNT(a) FROM Annonces a WHERE a.estActive = true GROUP BY a.ville")
    List<Object[]> countAnnoncesByVille();

    // ===== Géolocalisation =====

    // Annonces proches d'une position (géolocalisation)
    @Query("SELECT a FROM Annonces a WHERE a.estActive = true AND " +
            "a.latitude IS NOT NULL AND a.longitude IS NOT NULL AND " +
            "((6371 * acos(cos(radians(:latitude)) * cos(radians(a.latitude)) * " +
            "cos(radians(a.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(a.latitude)))) < :rayon)")
    List<Annonces> findAnnoncesProches(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("rayon") Double rayon
    );

    // ===== Villes et quartiers =====

    // Rechercher les annonces actives par ville
    @Query("SELECT a FROM Annonces a WHERE a.ville = :ville AND a.estActive = true")
    List<Annonces> findAnnoncesActivesByVille(@Param("ville") String ville);

    // Récupérer toutes les villes distinctes
    @Query("SELECT DISTINCT a.ville FROM Annonces a WHERE a.estActive = true AND a.ville IS NOT NULL ORDER BY a.ville")
    List<String> findAllVillesDistinct();

    // Récupérer tous les quartiers d'une ville
    @Query("SELECT DISTINCT a.ville FROM Annonces a WHERE a.ville = :ville AND a.estActive = true AND a.ville IS NOT NULL ORDER BY a.ville")
    List<String> findQuartiersByVille(@Param("ville") String ville);

    // Récupérer tous les types d'annonces
    @Query("SELECT DISTINCT a.typeAnnonce FROM Annonces a WHERE a.estActive = true AND a.typeAnnonce IS NOT NULL ORDER BY a.typeAnnonce")
    List<String> findAllTypesAnnonces();

    // ===== Prix =====

    // Récupérer le prix minimum
    @Query("SELECT MIN(a.prix) FROM Annonces a WHERE a.estActive = true")
    Double findMinPrix();

    // Récupérer le prix maximum
    @Query("SELECT MAX(a.prix) FROM Annonces a WHERE a.estActive = true")
    Double findMaxPrix();

    // Récupérer le prix moyen
    @Query("SELECT AVG(a.prix) FROM Annonces a WHERE a.estActive = true")
    Double findAvgPrix();
}