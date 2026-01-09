package com.example.backend.controllers;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.services.implementations.EquipementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/equipement")
public class EquipementController {

    private static final Logger logger = LoggerFactory.getLogger(EquipementController.class);

    private final EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody EquipementDTO equipementDTO) {
        logger.info("Création d'un équipement : nom={}", equipementDTO.getNom());
        try {
            EquipementDTO saved = equipementService.save(equipementDTO);
            logger.info("Équipement créé avec succès : id={}", saved.getId());
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.warn("Erreur lors de la création de l'équipement : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipementDTO equipementDTO) {
        logger.info("Mise à jour de l'équipement id={}", id);
        try {
            EquipementDTO updated = equipementService.update(id, equipementDTO);
            logger.info("Équipement mis à jour avec succès id={}", id);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Erreur lors de la mise à jour de l'équipement id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("Suppression de l'équipement id={}", id);
        try {
            equipementService.deleteById(id);
            logger.info("Équipement supprimé avec succès id={}", id);
            return new ResponseEntity<>("Équipement supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Erreur lors de la suppression de l'équipement id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        logger.info("Récupération de l'équipement id={}", id);
        try {
            EquipementDTO equipement = equipementService.getById(id);
            logger.info("Équipement récupéré id={}", id);
            return new ResponseEntity<>(equipement, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Erreur lors de la récupération de l'équipement id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<EquipementDTO>> getAll() {
        logger.info("Récupération de tous les équipements");
        try {
            List<EquipementDTO> result = equipementService.getAll();
            logger.info("Nombre d'équipements récupérés : {}", result.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Erreur lors de la récupération des équipements : {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
