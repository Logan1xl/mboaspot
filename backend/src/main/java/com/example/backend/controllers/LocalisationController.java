package com.example.backend.controllers;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.services.implementations.LocalisationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/localisation")
public class LocalisationController {

    private final LocalisationService localisationService;

    public LocalisationController(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    // 1. CRÉER (POST)
    @PostMapping
    public ResponseEntity<LocalisationDTO> addLocalisation(@RequestBody LocalisationDTO localisationDTO) {
        try {
            LocalisationDTO savedLocalisation = localisationService.save(localisationDTO);
            return new ResponseEntity<>(savedLocalisation, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides pour la création", e);
        }
    }

    // 2. RÉCUPÉRER TOUT (GET ALL)
    @GetMapping("/all")
    public ResponseEntity<List<LocalisationDTO>> getAllLocalisation() {
        try {
            List<LocalisationDTO> localisations = localisationService.getAll();
            return new ResponseEntity<>(localisations, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération de la liste", e);
        }
    }

    // 3. RÉCUPÉRER PAR ID (GET BY ID)
    @GetMapping("/{id}")
    public ResponseEntity<LocalisationDTO> getLocalisationById(@PathVariable Long id) {
        try {
            LocalisationDTO dto = localisationService.getById(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Renvoie 404 si l'élément n'existe pas dans la base
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localisation introuvable avec l'ID : " + id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur serveur", e);
        }
    }

    // 4. METTRE À JOUR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<LocalisationDTO> updateLocalisation(@PathVariable Long id, @RequestBody LocalisationDTO localisationDTO) {
        try {
            LocalisationDTO updatedLocalisation = localisationService.update(id, localisationDTO);
            return new ResponseEntity<>(updatedLocalisation, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible de mettre à jour : ID inexistant");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour", e);
        }
    }

    // 5. SUPPRIMER (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalisation(@PathVariable Long id) {
        try {
            localisationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Suppression impossible : ID " + id + " introuvable");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression", e);
        }
    }
}