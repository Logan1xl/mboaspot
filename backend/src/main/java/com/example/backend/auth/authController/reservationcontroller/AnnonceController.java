package com.example.backend.auth.authController.reservationcontroller;


import com.example.backend.entityDTO.reservationdto.AnnonceRequestDTO;
import com.example.backend.entityDTO.reservationdto.AnnonceResponseDTO;
import com.example.backend.reservationservice.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/annonces")
@CrossOrigin(origins = "*")


public class AnnonceController {


    @Autowired
    private AnnonceService annonceService;

    // Créer une annonce
    @PostMapping
    public ResponseEntity<AnnonceResponseDTO> creerAnnonce(@RequestBody AnnonceRequestDTO request) {
        AnnonceResponseDTO annonce = annonceService.creerAnnonce(request);
        return ResponseEntity.ok(annonce);
    }

    // Obtenir une annonce par ID
    @GetMapping("/{id}")
    public ResponseEntity<AnnonceResponseDTO> obtenirAnnonce(@PathVariable Long id) {
        AnnonceResponseDTO annonce = annonceService.obtenirAnnonce(id);
        return ResponseEntity.ok(annonce);
    }

    // Obtenir toutes les annonces actives
    @GetMapping
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesActives() {
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesActives();
        return ResponseEntity.ok(annonces);
    }

    // Obtenir les annonces d'un propriétaire
    @GetMapping("/proprietaire/{proprietaireId}")
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesProprietaire(@PathVariable Long proprietaireId) {
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesProprietaire(proprietaireId);
        return ResponseEntity.ok(annonces);
    }

    // Mettre à jour une annonce
    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponseDTO> mettreAJourAnnonce(
            @PathVariable Long id,
            @RequestBody AnnonceRequestDTO request) {
        AnnonceResponseDTO annonce = annonceService.mettreAJourAnnonce(id, request);
        return ResponseEntity.ok(annonce);
    }

    // Activer/Désactiver une annonce
    @PutMapping("/{id}/statut")
    public ResponseEntity<AnnonceResponseDTO> changerStatutAnnonce(
            @PathVariable Long id,
            @RequestParam Boolean estActive) {
        AnnonceResponseDTO annonce = annonceService.changerStatutAnnonce(id, estActive);
        return ResponseEntity.ok(annonce);
    }

    // Supprimer (désactiver) une annonce
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerAnnonce(@PathVariable Long id) {
        annonceService.supprimerAnnonce(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
