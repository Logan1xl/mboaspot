package com.example.backend.controllers;

import com.example.backend.dto.AvisDTO;
import com.example.backend.services.implementations.AvisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avis")
public class AvisController {
    private AvisService avisService;

    public AvisController(AvisService avisService) {
        this.avisService = avisService;
    }

    @PostMapping
    public ResponseEntity<?> createAvis(AvisDTO avisDTO){
        try{
            return new ResponseEntity<>(avisService.ajouterAvis(avisDTO), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllAvis(){
        try{
            return new ResponseEntity<>(avisService.listerAvis(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAvisById(Long id){
        try{
            return new ResponseEntity<>(avisService.obtenirAvisParId(id), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAvis(@PathVariable Long id, @RequestBody AvisDTO avisDTO){
        try{
            return new ResponseEntity<>(avisService.mettreAJourAvis(id, avisDTO), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAvis(@PathVariable Long id){
        try{
            avisService.supprimerAvis(id);
            return new ResponseEntity<>("Avis supprimé avec succès", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
