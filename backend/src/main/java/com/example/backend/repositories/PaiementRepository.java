package com.example.backend.repositories;

import com.example.backend.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour les paiements
 * @author Wulfrid MBONGO
 */
@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    /**
     * Trouve tous les paiements d'une réservation
     */
    List<Paiement> findByIdReservation_Id(Long idReservation);

    /**
     * Trouve un paiement par son ID de transaction
     */
    Optional<Paiement> findByIdTransaction(String idTransaction);

    /**
     * Trouve tous les paiements par statut
     */
    List<Paiement> findByStatut(String statut);

    /**
     * Trouve tous les paiements par méthode
     */
    List<Paiement> findByMethode(String methode);

    /**
     * Trouve tous les paiements d'un voyageur
     */
    @Query("SELECT p FROM Paiement p WHERE p.idReservation.idVoyageur.id = :idVoyageur")
    List<Paiement> findByIdVoyageur(@Param("idVoyageur") Long idVoyageur);

    /**
     * Calcule le montant total des paiements pour une réservation
     */
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.idReservation.id = :idReservation AND p.statut = :statut")
    Double sumMontantByReservationAndStatut(@Param("idReservation") Long idReservation,
                                            @Param("statut") String statut);
}