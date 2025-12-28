package com.example.backend.repositories;
import com.example.backend.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour les réservations
 * @author Wulfrid MBONGO
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Trouve une réservation par son code de confirmation
     */
    Optional<Reservation> findByCodeConfirmation(String codeConfirmation);
    // Trouve les réservations qui chevauchent
    @Query("SELECT r FROM Reservation r WHERE r.annonce.id = :annonceId " +
            "AND r.statut IN ('EN_ATTENTE', 'CONFIRMEE') " +
            "AND r.dateDebut <= :dateFin " +
            "AND r.dateFin >= :dateDebut")
    List<Reservation> findReservationsChevauchantes(
            @Param("annonceId") Long annonceId,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );

    // Réservations d'un voyageur
    List<Reservation> findByIdVoyageur_IdOrderByDateDebutDesc(Long voyageurId);

}