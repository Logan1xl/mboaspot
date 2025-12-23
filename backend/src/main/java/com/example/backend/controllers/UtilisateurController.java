package com.example.backend.controllers;

import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.services.implementation.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UtilisateurController {


    private final UtilisateurService utilisateurService;


    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Long id) {
        try {
            UtilisateurDTO utilisateurDTO  = utilisateurService.getUtilisateurById(id);
            return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            UtilisateurDTO createdUtilisateur = utilisateurService.createUtilisateur(utilisateurDTO);
            return new ResponseEntity<>(createdUtilisateur, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            UtilisateurDTO updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateurDTO);
            return new ResponseEntity<>(updatedUtilisateur, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return new ResponseEntity<>("Utilisateur supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
