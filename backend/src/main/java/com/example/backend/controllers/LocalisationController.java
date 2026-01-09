package com.example.backend.controllers;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.services.implementations.LocalisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/localisation")
public class LocalisationController {

    private static final Logger logger = LoggerFactory.getLogger(LocalisationController.class);

    private final LocalisationService localisationService;

    public LocalisationController(LocalisationService localisationService) {
        this.localisationService = localisationService;
    }

    // 1. CRÉER (POST)
    @PostMapping
    public ResponseEntity<LocalisationDTO> addLocalisation(@RequestBody LocalisationDTO localisationDTO) {
        logger.info("Création d'une localisation : {}", localisationDTO);
        try {
            LocalisationDTO savedLocalisation = localisationService.save(localisationDTO);
            logger.info("Localisation créée avec succès : {}", savedLocalisation);
            return new ResponseEntity<>(savedLocalisation, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la localisation : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données invalides pour la création", e);
        }
    }

    // 2. RÉCUPÉRER TOUT (GET ALL)
    @GetMapping("/all")
    public ResponseEntity<List<LocalisationDTO>> getAllLocalisation() {
        logger.info("Récupération de toutes les localisations");
        try {
            List<LocalisationDTO> localisations = localisationService.getAll();
            logger.info("Nombre de localisations récupérées : {}", localisations.size());
            return new ResponseEntity<>(localisations, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des localisations : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération de la liste", e);
        }
    }

    // 3. RÉCUPÉRER PAR ID (GET BY ID)
    @GetMapping("/{id}")
    public ResponseEntity<LocalisationDTO> getLocalisationById(@PathVariable Long id) {
        logger.info("Récupération de la localisation avec ID : {}", id);
        try {
            LocalisationDTO dto = localisationService.getById(id);
            logger.info("Localisation trouvée : {}", dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Localisation introuvable avec ID : {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Localisation introuvable avec l'ID : " + id);
        } catch (Exception e) {
            logger.error("Erreur serveur lors de la récupération de l'ID {} : {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur serveur", e);
        }
    }

    // 4. METTRE À JOUR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<LocalisationDTO> updateLocalisation(@PathVariable Long id, @RequestBody LocalisationDTO localisationDTO) {
        logger.info("Mise à jour de la localisation ID {} : {}", id, localisationDTO);
        try {
            LocalisationDTO updatedLocalisation = localisationService.update(id, localisationDTO);
            logger.info("Localisation mise à jour avec succès : {}", updatedLocalisation);
            return new ResponseEntity<>(updatedLocalisation, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Impossible de mettre à jour la localisation : ID {} inexistant", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossible de mettre à jour : ID inexistant");
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la localisation ID {} : {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour", e);
        }
    }

    // 5. SUPPRIMER (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalisation(@PathVariable Long id) {
        logger.info("Suppression de la localisation ID : {}", id);
        try {
            localisationService.delete(id);
            logger.info("Localisation supprimée avec succès ID : {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (RuntimeException e) {
            logger.warn("Suppression impossible, localisation introuvable ID : {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Suppression impossible : ID " + id + " introuvable");
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la localisation ID {} : {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression", e);
        }
    }
}
