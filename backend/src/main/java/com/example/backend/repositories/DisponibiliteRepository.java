package com.example.backend.repositories;

import com.example.backend.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    // Trouver toutes les disponibilités d'une annonce
    List<Disponibilite> findByIdAnnonceId(Long annonceId);

    // Vérifier la disponibilité pour une période donnée
    @Query("SELECT d FROM Disponibilite d WHERE d.idAnnonce.id = :annonceId AND " +
            "d.estDisponible = true AND " +
            "d.dateDebut <= :dateFin AND d.dateFin >= :dateDebut")
    List<Disponibilite> findDisponibilitePourPeriode(
            @Param("annonceId") Long annonceId,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );

    // Trouver les périodes non disponibles
    List<Disponibilite> findByIdAnnonceIdAndEstDisponibleFalse(Long annonceId);

    // Trouver les disponibilités avec surcharge
    List<Disponibilite> findByIdAnnonceIdAndPrixSurchargeIsNotNull(Long annonceId);
}