package com.example.backend.controllers;

import com.example.backend.dto.AnnonceDTO;
import com.example.backend.dto.AnnonceRequestDTO;
import com.example.backend.dto.AnnonceResponseDTO;
import com.example.backend.dto.DisponibiliteDTO;
import com.example.backend.dto.LocalisationDTO;
import com.example.backend.dto.RechercheDTO;
import com.example.backend.services.implementations.AnnonceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/annonces")
@CrossOrigin(origins = "*")
public class AnnonceController {

    private static final Logger logger = LoggerFactory.getLogger(AnnonceController.class);

    @Autowired
    private AnnonceService annonceService;

    // ===== CRUD Annonces (version simplifiée) =====

    @PostMapping("/v1")
    public ResponseEntity<AnnonceResponseDTO> creerAnnonce(@RequestBody AnnonceRequestDTO request) {
        logger.info("Création d'une annonce pour le propriétaire ID={}", request.getProprietaireId());
        AnnonceResponseDTO annonce = annonceService.creerAnnonce(request);
        logger.info("Annonce créée avec succès ID={}", annonce.getId());
        return ResponseEntity.ok(annonce);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<AnnonceResponseDTO> obtenirAnnonce(@PathVariable Long id) {
        logger.info("Récupération de l'annonce ID={}", id);
        AnnonceResponseDTO annonce = annonceService.obtenirAnnonce(id);
        logger.info("Annonce récupérée ID={}", id);
        return ResponseEntity.ok(annonce);
    }

    @GetMapping("/v1/actives")
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesActives() {
        logger.info("Récupération des annonces actives");
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesActives();
        logger.info("{} annonces actives récupérées", annonces.size());
        return ResponseEntity.ok(annonces);
    }

    @GetMapping("/v1/proprietaire/{proprietaireId}")
    public ResponseEntity<List<AnnonceResponseDTO>> obtenirAnnoncesProprietaire(@PathVariable Long proprietaireId) {
        logger.info("Récupération des annonces du propriétaire ID={}", proprietaireId);
        List<AnnonceResponseDTO> annonces = annonceService.obtenirAnnoncesProprietaire(proprietaireId);
        logger.info("{} annonces récupérées pour le propriétaire ID={}", annonces.size(), proprietaireId);
        return ResponseEntity.ok(annonces);
    }

    @PutMapping("/v1/{id}")
    public ResponseEntity<AnnonceResponseDTO> mettreAJourAnnonce(
            @PathVariable Long id,
            @RequestBody AnnonceRequestDTO request) {
        logger.info("Mise à jour de l'annonce ID={}", id);
        AnnonceResponseDTO annonce = annonceService.mettreAJourAnnonce(id, request);
        logger.info("Annonce mise à jour ID={}", id);
        return ResponseEntity.ok(annonce);
    }

    @PutMapping("/v1/{id}/statut")
    public ResponseEntity<AnnonceResponseDTO> changerStatutAnnonce(
            @PathVariable Long id,
            @RequestParam Boolean estActive) {
        logger.info("Changement de statut de l'annonce ID={} à estActive={}", id, estActive);
        AnnonceResponseDTO annonce = annonceService.changerStatutAnnonce(id, estActive);
        logger.info("Statut changé pour l'annonce ID={}", id);
        return ResponseEntity.ok(annonce);
    }

    @DeleteMapping("/v1/{id}")
    public ResponseEntity<Void> supprimerAnnonce(@PathVariable Long id) {
        logger.info("Suppression de l'annonce ID={}", id);
        annonceService.supprimerAnnonce(id);
        logger.info("Annonce supprimée ID={}", id);
        return ResponseEntity.noContent().build();
    }

    // ===== CRUD Annonces (version complète DTO) =====

    @GetMapping
    public ResponseEntity<List<AnnonceDTO>> getAllAnnonces() {
        logger.info("Récupération de toutes les annonces");
        List<AnnonceDTO> result = annonceService.getAllAnnonces();
        logger.info("{} annonces récupérées", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<AnnonceDTO>> getAnnoncesActives() {
        logger.info("Récupération de toutes les annonces actives");
        List<AnnonceDTO> result = annonceService.getAnnoncesActives();
        logger.info("{} annonces actives récupérées", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable Long id) {
        logger.info("Récupération de l'annonce DTO ID={}", id);
        return annonceService.getAnnonceById(id)
                .map(dto -> {
                    logger.info("Annonce DTO récupérée ID={}", id);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> {
                    logger.warn("Annonce DTO non trouvée ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> createAnnonce(@RequestBody AnnonceDTO dto) {
        logger.info("Création d'une annonce DTO");
        try {
            AnnonceDTO created = annonceService.createAnnonce(dto);
            logger.info("Annonce DTO créée ID={}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur création annonce DTO", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceDTO> updateAnnonce(
            @PathVariable Long id,
            @RequestBody AnnonceDTO dto) {
        logger.info("Mise à jour annonce DTO ID={}", id);
        try {
            AnnonceDTO updated = annonceService.updateAnnonce(id, dto);
            logger.info("Annonce DTO mise à jour ID={}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Erreur mise à jour annonce DTO ID={}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        logger.info("Suppression annonce DTO ID={}", id);
        try {
            annonceService.deleteAnnonce(id);
            logger.info("Annonce DTO supprimée ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur suppression annonce DTO ID={}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activerAnnonce(
            @PathVariable Long id,
            @RequestParam Boolean activer) {
        logger.info("Activation/desactivation annonce DTO ID={} activer={}", id, activer);
        try {
            annonceService.activerAnnonce(id, activer);
            logger.info("Annonce DTO ID={} est maintenant activer={}", id, activer);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erreur activation/desactivation annonce DTO ID={}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    // ===== Recherche avancée =====

    @PostMapping("/recherche")
    public ResponseEntity<List<AnnonceDTO>> rechercherAnnonces(@RequestBody RechercheDTO recherche) {
        logger.info("Recherche annonces avec critères : {}", recherche);
        List<AnnonceDTO> resultats = annonceService.rechercherAnnonces(recherche);
        logger.info("{} résultats trouvés", resultats.size());
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/top")
    public ResponseEntity<List<AnnonceDTO>> getTopAnnonces() {
        logger.info("Récupération des top annonces");
        List<AnnonceDTO> result = annonceService.getTopAnnonces();
        logger.info("{} top annonces récupérées", result.size());
        return ResponseEntity.ok(result);
    }

    // ===== Gestion Disponibilité =====

    @GetMapping("/{id}/disponibilites")
    public ResponseEntity<List<DisponibiliteDTO>> getDisponibilites(@PathVariable Long id) {
        logger.info("Récupération disponibilités pour annonce ID={}", id);
        List<DisponibiliteDTO> result = annonceService.getDisponibilites(id);
        logger.info("{} disponibilités récupérées pour annonce ID={}", result.size(), id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteDTO> addDisponibilite(@RequestBody DisponibiliteDTO dto) {

        try {
            DisponibiliteDTO created = annonceService.addDisponibilite(dto);
            logger.info("Disponibilite ajoutée ID={}", created.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            logger.error("Erreur ajout disponibilite", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/verifier-disponibilite")
    public ResponseEntity<Boolean> verifierDisponibilite(
            @PathVariable Long id,
            @RequestParam Date dateDebut,
            @RequestParam Date dateFin) {
        logger.info("Vérification disponibilité annonce ID={} du {} au {}", id, dateDebut, dateFin);
        boolean disponible = annonceService.verifierDisponibilite(id, dateDebut, dateFin);
        logger.info("Annonce ID={} disponible={}", id, disponible);
        return ResponseEntity.ok(disponible);
    }

    // ===== Gestion Localisation =====

    @GetMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> getLocalisation(@PathVariable Long id) {
        logger.info("Récupération localisation annonce ID={}", id);
        return annonceService.getLocalisation(id)
                .map(dto -> {
                    logger.info("Localisation récupérée annonce ID={}", id);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> {
                    logger.warn("Localisation non trouvée annonce ID={}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}/localisation")
    public ResponseEntity<LocalisationDTO> updateLocalisation(
            @PathVariable Long id,
            @RequestBody LocalisationDTO dto) {
        logger.info("Mise à jour localisation annonce ID={}", id);
        try {
            LocalisationDTO updated = annonceService.updateLocalisation(id, dto);
            logger.info("Localisation mise à jour annonce ID={}", id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            logger.error("Erreur mise à jour localisation annonce ID={}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    // ===== Gestion des exceptions =====

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException interceptée: {}", ex.getMessage(), ex);
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
