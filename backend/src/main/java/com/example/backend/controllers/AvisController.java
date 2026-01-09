package com.example.backend.controllers;

import com.example.backend.dto.AvisDTO;
import com.example.backend.services.implementations.AvisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/avis")
public class AvisController {

    private static final Logger logger = LoggerFactory.getLogger(AvisController.class);

    private AvisService avisService;

    public AvisController(AvisService avisService) {
        this.avisService = avisService;
    }

    @PostMapping
    public ResponseEntity<?> createAvis(@RequestBody AvisDTO avisDTO){
        logger.info("Tentative de création d'avis pour voyageurId={}",
                avisDTO.getIdVoyageur() != null ? avisDTO.getIdVoyageur().getId() : "null");

        try {
            Object result = avisService.ajouterAvis(avisDTO);
            logger.info("Avis créé avec succès pour voyageurId={}",
                    avisDTO.getIdVoyageur() != null ? avisDTO.getIdVoyageur().getId() : "null");
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e){
            logger.warn("Erreur lors de la création de l'avis : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAvis(){
        logger.info("Récupération de tous les avis");
        try{
            return new ResponseEntity<>(avisService.listerAvis(), HttpStatus.OK);
        } catch (Exception e){
            logger.warn("Erreur lors de la récupération des avis : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAvisById(@PathVariable Long id){
        logger.info("Récupération de l'avis avec id={}", id);
        try{
            return new ResponseEntity<>(avisService.obtenirAvisParId(id), HttpStatus.OK);
        } catch (Exception e){
            logger.warn("Erreur lors de la récupération de l'avis id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAvis(@PathVariable Long id, @RequestBody AvisDTO avisDTO){
        logger.info("Mise à jour de l'avis id={}", id);
        try{
            Object result = avisService.mettreAJourAvis(id, avisDTO);
            logger.info("Avis mis à jour avec succès id={}", id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            logger.warn("Erreur lors de la mise à jour de l'avis id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAvis(@PathVariable Long id){
        logger.info("Suppression de l'avis id={}", id);
        try{
            avisService.supprimerAvis(id);
            logger.info("Avis supprimé avec succès id={}", id);
            return new ResponseEntity<>("Avis supprimé avec succès", HttpStatus.OK);
        } catch (Exception e){
            logger.warn("Erreur lors de la suppression de l'avis id={} : {}", id, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
