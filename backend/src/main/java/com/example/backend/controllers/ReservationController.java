package com.example.backend.controllers;

import com.example.backend.dto.DisponibiliteRequestDTO;
import com.example.backend.dto.ReservationRequestDTO;
import com.example.backend.dto.ReservationResponseDTO;
import com.example.backend.services.implementations.ReservationService;
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

        @Autowired
        private ReservationService reservationService;

        // Vérifier disponibilité
        @PostMapping("/disponibilite")
        public ResponseEntity<Boolean> verifierDisponibilite(@RequestBody DisponibiliteRequestDTO request) {
            boolean disponible = reservationService.verifierDisponibilite(request);
            return ResponseEntity.ok(disponible);
        }

        // Créer une réservation
        @PostMapping
        public ResponseEntity<ReservationResponseDTO> creerReservation(@RequestBody ReservationRequestDTO request) {
            ReservationResponseDTO reservation = reservationService.creerReservation(request);
            return ResponseEntity.ok(reservation);
        }

        // Obtenir une réservation par ID
        @GetMapping("/{id}")
        public ResponseEntity<ReservationResponseDTO> obtenirReservation(@PathVariable Long id) {
            ReservationResponseDTO reservation = reservationService.obtenirReservation(id);
            return ResponseEntity.ok(reservation);
        }

        // Obtenir les réservations d'un voyageur
        @GetMapping("/voyageur/{voyageurId}")
        public ResponseEntity<List<ReservationResponseDTO>> obtenirReservationsVoyageur(@PathVariable Long voyageurId) {
            List<ReservationResponseDTO> reservations = reservationService.obtenirReservationsVoyageur(voyageurId);
            return ResponseEntity.ok(reservations);
        }

        // Annuler une réservation
        @PutMapping("/{id}/annuler")
        public ResponseEntity<ReservationResponseDTO> annulerReservation(@PathVariable Long id) {
            ReservationResponseDTO reservation = reservationService.annulerReservation(id);
            return ResponseEntity.ok(reservation);
        }

        // Confirmer une réservation (après paiement)
        @PutMapping("/{id}/confirmer")
        public ResponseEntity<ReservationResponseDTO> confirmerReservation(@PathVariable Long id) {
            ReservationResponseDTO reservation = reservationService.confirmerReservation(id);
            return ResponseEntity.ok(reservation);
        }

        // Obtenir par code de confirmation
        @GetMapping("/confirmation/{code}")
        public ResponseEntity<ReservationResponseDTO> obtenirParCodeConfirmation(@PathVariable String code) {
            ReservationResponseDTO reservation = reservationService.obtenirParCodeConfirmation(code);
            return ResponseEntity.ok(reservation);
        }
        @GetMapping
        public ResponseEntity<List<ReservationResponseDTO>> obtenirTouteResvervation(){
            return ResponseEntity.ok(reservationService.obtenirTouteResvervation());
        }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

