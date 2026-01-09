package com.example.backend.controllers;

import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.services.implementations.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUtilisateurs() {
        logger.info("Récupération de tous les utilisateurs");
        var list = utilisateurService.getAllUtilisateurs();
        logger.info("Nombre d'utilisateurs récupérés : {}", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Long id) {
        logger.info("Récupération de l'utilisateur ID : {}", id);
        try {
            UtilisateurDTO utilisateurDTO = utilisateurService.getUtilisateurById(id);
            logger.info("Utilisateur trouvé : {}", utilisateurDTO);
            return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'utilisateur ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        logger.info("Création d'un nouvel utilisateur : {}", utilisateurDTO);
        try {
            UtilisateurDTO createdUtilisateur = utilisateurService.createUtilisateur(utilisateurDTO);
            logger.info("Utilisateur créé avec succès : {}", createdUtilisateur);
            return new ResponseEntity<>(createdUtilisateur, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        logger.info("Mise à jour de l'utilisateur ID {} : {}", id, utilisateurDTO);
        try {
            UtilisateurDTO updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateurDTO);
            logger.info("Utilisateur mis à jour avec succès : {}", updatedUtilisateur);
            return new ResponseEntity<>(updatedUtilisateur, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        logger.info("Suppression de l'utilisateur ID : {}", id);
        try {
            utilisateurService.deleteUtilisateur(id);
            logger.info("Utilisateur supprimé avec succès ID : {}", id);
            return new ResponseEntity<>("Utilisateur supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'utilisateur ID {} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
