package com.example.backend.auth.authController.reservationcontroller;

import com.example.backend.entityDTO.reservationdto.DisponibiliteRequestDTO;
import com.example.backend.entityDTO.reservationdto.ReservationRequestDTO;
import com.example.backend.entityDTO.reservationdto.ReservationResponseDTO;
import com.example.backend.reservationservice.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
}
