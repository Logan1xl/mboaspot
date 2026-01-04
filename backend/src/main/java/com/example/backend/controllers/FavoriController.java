package com.example.backend.controllers;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.services.implementations.FavoriService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favori")
public class FavoriController {
    private FavoriService favoriService;

    public FavoriController(FavoriService favoriService) {
        this.favoriService = favoriService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFavorisByUserId(@PathVariable Long id) {
        try {
            FavoriDTO favoris = favoriService.obtenirFavoriParId(id);
            return new ResponseEntity<>(favoris, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createFavori(@RequestBody FavoriDTO favoriDTO) {
        try {
            FavoriDTO createdFavori = favoriService.ajouterFavori(favoriDTO);
            return new ResponseEntity<>(createdFavori, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFavori(@PathVariable Long id, @RequestBody FavoriDTO favoriDTO) {
        try {
            FavoriDTO updatedFavori = favoriService.mettreAJourFavori(id, favoriDTO);
            return new ResponseEntity<>(updatedFavori, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavori(@PathVariable Long id) {
        try {
            favoriService.supprimerFavori(id);
            return new ResponseEntity<>("Favori supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}