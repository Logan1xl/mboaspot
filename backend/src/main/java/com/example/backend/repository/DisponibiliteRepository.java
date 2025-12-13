package com.example.backend.repository;

import com.example.backend.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    // Trouve les disponibilités qui couvrent une période
    @Query("SELECT d FROM Disponibilite d WHERE d.idAnnonce.id = :annonceId " +
            "AND d.estDisponible = true " +
            "AND d.dateDebut <= :dateDebut " +
            "AND d.dateFin >= :dateFin")
    List<Disponibilite> findDisponibiliteCouvrante(
            @Param("annonceId") Long annonceId,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );

    // Trouve les périodes bloquées qui chevauchent
    @Query("SELECT d FROM Disponibilite d WHERE d.idAnnonce.id = :annonceId " +
            "AND d.estDisponible = false " +
            "AND d.dateDebut <= :dateFin " +
            "AND d.dateFin >= :dateDebut")
    List<Disponibilite> findPeriodesBloquees(
            @Param("annonceId") Long annonceId,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );
}