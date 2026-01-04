package com.example.backend.controllers;

import com.example.backend.dto.PaiementDTO;
import com.example.backend.services.implementations.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des paiements
 * @author Wulfrid MBONGO
 */
@RestController
@RequestMapping("/api/paiement")
@CrossOrigin(origins = "*")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    /**
     * Crée un nouveau paiement
     * POST /api/paiement
     */
    @PostMapping
    public ResponseEntity<?> creerPaiement(@RequestBody PaiementDTO paiementDTO) {
        try {
            PaiementDTO paiementCree = paiementService.creerPaiement(paiementDTO);
            return new ResponseEntity<>(paiementCree, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la création du paiement: " + e.getMessage()));
        }
    }

    /**
     * Récupère tous les paiements
     * GET /api/paiement
     */
    @GetMapping
    public ResponseEntity<?> obtenirTousLesPaiements() {
        try {
            List<PaiementDTO> paiements = paiementService.obtenirTousLesPaiements();
            return ResponseEntity.ok(paiements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la récupération des paiements: " + e.getMessage()));
        }
    }

    /**
     * Récupère un paiement par son ID
     * GET /api/paiement/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirPaiementParId(@PathVariable Long id) {
        try {
            PaiementDTO paiement = paiementService.obtenirPaiementParId(id);
            return ResponseEntity.ok(paiement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la récupération du paiement: " + e.getMessage()));
        }
    }

    /**
     * Récupère tous les paiements d'une réservation
     * GET /api/paiement/reservation/{idReservation}
     */
    @GetMapping("/reservation/{idReservation}")
    public ResponseEntity<?> obtenirPaiementsParReservation(@PathVariable Long idReservation) {
        try {
            List<PaiementDTO> paiements = paiementService.obtenirPaiementsParReservation(idReservation);
            return ResponseEntity.ok(paiements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la récupération des paiements: " + e.getMessage()));
        }
    }

    /**
     * Récupère un paiement par son ID de transaction
     * GET /api/paiement/transaction/{idTransaction}
     */
    @GetMapping("/transaction/{idTransaction}")
    public ResponseEntity<?> obtenirPaiementParIdTransaction(@PathVariable String idTransaction) {
        try {
            PaiementDTO paiement = paiementService.obtenirPaiementParIdTransaction(idTransaction);
            return ResponseEntity.ok(paiement);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la récupération du paiement: " + e.getMessage()));
        }
    }

    /**
     * Récupère tous les paiements par statut
     * GET /api/paiement/statut/{statut}
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<?> obtenirPaiementsParStatut(@PathVariable String statut) {
        try {
            List<PaiementDTO> paiements = paiementService.obtenirPaiementsParStatut(statut);
            return ResponseEntity.ok(paiements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la récupération des paiements: " + e.getMessage()));
        }
    }

    /**
     * Met à jour un paiement
     * PUT /api/paiement/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourPaiement(@PathVariable Long id, @RequestBody PaiementDTO paiementDTO) {
        try {
            PaiementDTO paiementMisAJour = paiementService.mettreAJourPaiement(id, paiementDTO);
            return ResponseEntity.ok(paiementMisAJour);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la mise à jour du paiement: " + e.getMessage()));
        }
    }

    /**
     * Met à jour le statut d'un paiement
     * PATCH /api/paiement/{id}/statut
     */
    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> mettreAJourStatutPaiement(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String nouveauStatut = body.get("statut");
            if (nouveauStatut == null || nouveauStatut.isEmpty()) {
                return ResponseEntity.badRequest().body(creerMessageErreur("Le statut est obligatoire"));
            }

            PaiementDTO paiementMisAJour = paiementService.mettreAJourStatutPaiement(id, nouveauStatut);
            return ResponseEntity.ok(paiementMisAJour);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la mise à jour du statut: " + e.getMessage()));
        }
    }

    /**
     * Supprime un paiement
     * DELETE /api/paiement/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerPaiement(@PathVariable Long id) {
        try {
            paiementService.supprimerPaiement(id);
            return ResponseEntity.ok(creerMessageSucces("Paiement supprimé avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la suppression du paiement: " + e.getMessage()));
        }
    }

    /**
     * Calcule le montant total payé pour une réservation
     * GET /api/paiement/reservation/{idReservation}/montant-total
     */
    @GetMapping("/reservation/{idReservation}/montant-total")
    public ResponseEntity<?> calculerMontantTotalPaye(@PathVariable Long idReservation) {
        try {
            Double montantTotal = paiementService.calculerMontantTotalPaye(idReservation);
            Map<String, Object> response = new HashMap<>();
            response.put("idReservation", idReservation);
            response.put("montantTotalPaye", montantTotal);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors du calcul du montant total: " + e.getMessage()));
        }
    }

    /**
     * Vérifie si une réservation est entièrement payée
     * GET /api/paiement/reservation/{idReservation}/est-paye
     */
    @GetMapping("/reservation/{idReservation}/est-paye")
    public ResponseEntity<?> verifierSiEntierementPaye(@PathVariable Long idReservation) {
        try {
            boolean estPaye = paiementService.estEntierementPaye(idReservation);
            Map<String, Object> response = new HashMap<>();
            response.put("idReservation", idReservation);
            response.put("estEntierementPaye", estPaye);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(creerMessageErreur(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(creerMessageErreur("Erreur lors de la vérification du paiement: " + e.getMessage()));
        }
    }

    /**
     * Crée un message d'erreur formaté
     */
    private Map<String, String> creerMessageErreur(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("erreur", message);
        return response;
    }

    /**
     * Crée un message de succès formaté
     */
    private Map<String, String> creerMessageSucces(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}