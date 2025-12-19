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


    @PostMapping
    public ResponseEntity<LocalisationDTO> addLocalisation(@RequestBody LocalisationDTO localisationDTO) {
        try {
            LocalisationDTO savedLocalisation = localisationService.save(localisationDTO);
            return new ResponseEntity<>(savedLocalisation, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add localisation", e);
        }
    }

    @GetMapping()
    public ResponseEntity<List<LocalisationDTO>> getAllLocalisation() {
        try {
            List<LocalisationDTO> localisations = localisationService.getAll();
            return new ResponseEntity<>(localisations, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve localisations", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalisationDTO> updateLocalisation(@PathVariable Long id, @RequestBody LocalisationDTO localisationDTO) {
        try {
            LocalisationDTO updatedLocalisation = localisationService.update(id, localisationDTO);
            return new ResponseEntity<>(updatedLocalisation, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update localisation", e);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalisation(@PathVariable Long id) {
        try {
            localisationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete localisation", e);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<LocalisationDTO> getLocalisationById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(localisationService.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve localisation", e);
        }
    }

}






