package com.example.backend.repository;

import com.example.backend.entities.Annonces;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnoncesRepository extends JpaRepository<Annonces,Long> {
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
