package com.example.backend.repositories;
import com.example.backend.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}