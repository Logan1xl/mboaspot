package com.example.backend.controllers;

import com.example.backend.dto.DisponibiliteRequestDTO;
import com.example.backend.dto.ReservationRequestDTO;
import com.example.backend.dto.ReservationResponseDTO;
import com.example.backend.services.implementations.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    // Vérifier disponibilité
    @PostMapping("/disponibilite")
    public ResponseEntity<Boolean> verifierDisponibilite(@RequestBody DisponibiliteRequestDTO request) {
        logger.info("Vérification de disponibilité pour : {}", request);
        boolean disponible = reservationService.verifierDisponibilite(request);
        logger.info("Résultat disponibilité : {}", disponible);
        return ResponseEntity.ok(disponible);
    }

    // Créer une réservation
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> creerReservation(@RequestBody ReservationRequestDTO request) {
        logger.info("Création d'une réservation : {}", request);
        ReservationResponseDTO reservation = reservationService.creerReservation(request);
        logger.info("Réservation créée : {}", reservation);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> obtenirReservation(@PathVariable Long id) {
        logger.info("Récupération de la réservation ID : {}", id);
        ReservationResponseDTO reservation = reservationService.obtenirReservation(id);
        logger.info("Réservation trouvée : {}", reservation);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir les réservations d'un voyageur
    @GetMapping("/voyageur/{voyageurId}")
    public ResponseEntity<List<ReservationResponseDTO>> obtenirReservationsVoyageur(@PathVariable Long voyageurId) {
        logger.info("Récupération des réservations pour voyageur ID : {}", voyageurId);
        List<ReservationResponseDTO> reservations = reservationService.obtenirReservationsVoyageur(voyageurId);
        logger.info("Nombre de réservations trouvées : {}", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    // Annuler une réservation
    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponseDTO> annulerReservation(@PathVariable Long id) {
        logger.info("Annulation de la réservation ID : {}", id);
        ReservationResponseDTO reservation = reservationService.annulerReservation(id);
        logger.info("Réservation annulée : {}", reservation);
        return ResponseEntity.ok(reservation);
    }

    // Confirmer une réservation (après paiement)
    @PutMapping("/{id}/confirmer")
    public ResponseEntity<ReservationResponseDTO> confirmerReservation(@PathVariable Long id) {
        logger.info("Confirmation de la réservation ID : {}", id);
        ReservationResponseDTO reservation = reservationService.confirmerReservation(id);
        logger.info("Réservation confirmée : {}", reservation);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir par code de confirmation
    @GetMapping("/confirmation/{code}")
    public ResponseEntity<ReservationResponseDTO> obtenirParCodeConfirmation(@PathVariable String code) {
        logger.info("Récupération réservation par code de confirmation : {}", code);
        ReservationResponseDTO reservation = reservationService.obtenirParCodeConfirmation(code);
        logger.info("Réservation trouvée : {}", reservation);
        return ResponseEntity.ok(reservation);
    }

    // Obtenir toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> obtenirTouteResvervation() {
        logger.info("Récupération de toutes les réservations");
        List<ReservationResponseDTO> reservations = reservationService.obtenirTouteResvervation();
        logger.info("Nombre total de réservations : {}", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        logger.error("Erreur interne : {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
