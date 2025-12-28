package com.example.backend.controllers;

import com.example.backend.dto.*;
import com.example.backend.services.implementations.AnnonceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/annonces")
@CrossOrigin(origins = "*")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    // ===== CRUD Annonces (version simplifiée) =====

    @PostMapping("/v1")
    public ResponseEntity<AnnonceResponseDTO> creerAnnonce(@RequestBody AnnonceRequestDTO request) {
        AnnonceResponseDTO annonce = annonceService.creerAnnonce(request);
        return ResponseEntity.ok(annonce);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<AnnonceResponseDTO> obtenirAnnonce(@PathVariable Long id) {
        AnnonceResponseDTO annonce = annonceService.obtenirAnnonce(id);
        return ResponseEntity.ok(annonce);
    }

    @GetMapping("/v1/actives")
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesActives() {
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesActives();
        return ResponseEntity.ok(annonces);
    }

    @GetMapping("/v1/proprietaire/{proprietaireId}")
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesProprietaire(@PathVariable Long proprietaireId) {
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesProprietaire(proprietaireId);
        return ResponseEntity.ok(annonces);
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<AnnonceResponseDTO> mettreAJourAnnonce(
            @PathVariable Long id,
            @RequestBody AnnonceRequestDTO request) {
        AnnonceResponseDTO annonce = annonceService.mettreAJourAnnonce(id, request);
        return ResponseEntity.ok(annonce);
    }

    @PutMapping("/v1/{id}/statut")
    public ResponseEntity<AnnonceResponseDTO> changerStatutAnnonce(
            @PathVariable Long id,
            @RequestParam Boolean estActive) {
        AnnonceResponseDTO annonce = annonceService.changerStatutAnnonce(id, estActive);
        return ResponseEntity.ok(annonce);
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> supprimerAnnonce(@PathVariable Long id) {
        annonceService.supprimerAnnonce(id);
        return ResponseEntity.noContent().build();
    }

    // ===== CRUD Annonces (version complète DTO) =====

    @GetMapping
    public ResponseEntity<List<AnnonceDTO>> getAllAnnonces() {
        return ResponseEntity.ok(annonceService.getAllAnnonces());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<AnnonceDTO>> getAnnoncesActives() {
        return ResponseEntity.ok(annonceService.getAnnoncesActives());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable Long id) {
        return annonceService.getAnnonceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> createAnnonce(@RequestBody AnnonceDTO dto) {
        try {
            AnnonceDTO created = annonceService.createAnnonce(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> updateAnnonce(
            @PathVariable Long id,
            @RequestBody AnnonceDTO dto) {
        try {
            AnnonceDTO updated = annonceService.updateAnnonce(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        try {
            annonceService.deleteAnnonce(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerAnnonce(
            @PathVariable Long id,
            @RequestParam Boolean activer) {
        try {
            annonceService.activerAnnonce(id, activer);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== Recherche avancée =====

    @PostMapping("/recherche")
    public ResponseEntity<List<AnnonceDTO>> rechercherAnnonces(@RequestBody RechercheDTO recherche) {
        List<AnnonceDTO> resultats = annonceService.rechercherAnnonces(recherche);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/top")
    public ResponseEntity<List<AnnonceDTO>> getTopAnnonces() {
        return ResponseEntity.ok(annonceService.getTopAnnonces());
    }

    @GetMapping("/villes")
    public ResponseEntity<List<String>> getVillesDisponibles() {
        return ResponseEntity.ok(annonceService.getVillesDisponibles());
    }

    @GetMapping("/quartiers")
    public ResponseEntity<List<String>> getQuartiers(@RequestParam String ville) {
        return ResponseEntity.ok(annonceService.getQuartiersByVille(ville));
    }

    // ===== Gestion Disponibilité =====

    @GetMapping("/{id}/disponibilites")
    public ResponseEntity<List<DisponibiliteDTO>> getDisponibilites(@PathVariable Long id) {
        return ResponseEntity.ok(annonceService.getDisponibilites(id));
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteDTO> addDisponibilite(@RequestBody DisponibiliteDTO dto) {
        try {
            DisponibiliteDTO created = annonceService.addDisponibilite(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/verifier-disponibilite")
    public ResponseEntity<Boolean> verifierDisponibilite(
            @PathVariable Long id,
            @RequestParam Date dateDebut,
            @RequestParam Date dateFin) {
        boolean disponible = annonceService.verifierDisponibilite(id, dateDebut, dateFin);
        return ResponseEntity.ok(disponible);
    }

    // ===== Gestion Localisation =====

    @GetMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> getLocalisation(@PathVariable Long id) {
        return annonceService.getLocalisation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> updateLocalisation(
            @PathVariable Long id,
            @RequestBody LocalisationDTO dto) {
        try {
            LocalisationDTO updated = annonceService.updateLocalisation(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== Gestion des exceptions =====

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}