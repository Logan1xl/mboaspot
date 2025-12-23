package com.example.backend.controllers;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.services.implementations.EquipementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipement")
public class EquipementController {

    private final EquipementService equipementService;

    // Injection par constructeur (recommandé)
    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody EquipementDTO equipementDTO) {
        try {
            EquipementDTO saved = equipementService.save(equipementDTO);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipementDTO equipementDTO) {
        try {
            // Utilisation d'une méthode update (à créer dans votre service)
            EquipementDTO updated = equipementService.update(id, equipementDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            equipementService.deleteById(id); // Utilise le nom exact défini dans le service
            return new ResponseEntity<>("Équipement supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            EquipementDTO equipement = equipementService.getById(id); // Récupère l'ID précis
            return new ResponseEntity<>(equipement, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<EquipementDTO>> getAll() {
        try {
            return new ResponseEntity<>(equipementService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}