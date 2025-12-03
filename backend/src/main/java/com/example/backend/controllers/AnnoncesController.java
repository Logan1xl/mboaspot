package com.example.backend.controllers;

import com.example.backend.dto.*;
import com.example.backend.services.AnnoncesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@CrossOrigin(origins = "*")
public class AnnoncesController {

    @Autowired
    private AnnoncesService annoncesService;

    // ===== CRUD Annonces =====

    @GetMapping
    public ResponseEntity<List<AnnonceDTO>> getAllAnnonces() {
        return ResponseEntity.ok(annoncesService.getAllAnnonces());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<AnnonceDTO>> getAnnoncesActives() {
        return ResponseEntity.ok(annoncesService.getAnnoncesActives());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable Long id) {
        return annoncesService.getAnnonceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> createAnnonce(@RequestBody AnnonceDTO dto) {
        try {
            AnnonceDTO created = annoncesService.createAnnonce(dto);
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
            AnnonceDTO updated = annoncesService.updateAnnonce(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        try {
            annoncesService.deleteAnnonce(id);
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
            annoncesService.activerAnnonce(id, activer);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== Recherche =====

    @PostMapping("/recherche")
    public ResponseEntity<List<AnnonceDTO>> rechercherAnnonces(@RequestBody RechercheDTO recherche) {
        List<AnnonceDTO> resultats = annoncesService.rechercherAnnonces(recherche);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/top")
    public ResponseEntity<List<AnnonceDTO>> getTopAnnonces() {
        return ResponseEntity.ok(annoncesService.getTopAnnonces());
    }

    @GetMapping("/villes")
    public ResponseEntity<List<String>> getVillesDisponibles() {
        return ResponseEntity.ok(annoncesService.getVillesDisponibles());
    }

    @GetMapping("/quartiers")
    public ResponseEntity<List<String>> getQuartiers(@RequestParam String ville) {
        return ResponseEntity.ok(annoncesService.getQuartiersByVille(ville));
    }

    // ===== Disponibilité =====

    @GetMapping("/{id}/disponibilites")
    public ResponseEntity<List<DisponibiliteDTO>> getDisponibilites(@PathVariable Long id) {
        return ResponseEntity.ok(annoncesService.getDisponibilites(id));
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteDTO> addDisponibilite(@RequestBody DisponibiliteDTO dto) {
        try {
            DisponibiliteDTO created = annoncesService.addDisponibilite(dto);
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
        boolean disponible = annoncesService.verifierDisponibilite(id, dateDebut, dateFin);
        return ResponseEntity.ok(disponible);
    }

    // ===== Localisation =====

    @GetMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> getLocalisation(@PathVariable Long id) {
        return annoncesService.getLocalisation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> updateLocalisation(
            @PathVariable Long id,
            @RequestBody LocalisationDTO dto) {
        try {
            LocalisationDTO updated = annoncesService.updateLocalisation(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}