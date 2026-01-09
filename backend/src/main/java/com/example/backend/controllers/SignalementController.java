package com.example.backend.controllers;

import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.dto.SignalementResponseDTO;
import com.example.backend.entities.Signalement;
import com.example.backend.mappers.SignalementMapper;
import com.example.backend.services.implementations.SignalementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/signalements")
@CrossOrigin
public class SignalementController {

    private static final Logger logger = LoggerFactory.getLogger(SignalementController.class);

    private final SignalementService signalementService;

    public SignalementController(SignalementService signalementService) {
        this.signalementService = signalementService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<SignalementResponseDTO> creer(@RequestBody SignalementRequestDTO dto) {
        logger.info("Création d'un signalement : {}", dto);
        Signalement s = signalementService.creer(dto);
        SignalementResponseDTO response = SignalementMapper.toDTO(s);
        logger.info("Signalement créé : {}", response);
        return ResponseEntity.ok(response);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<SignalementResponseDTO>> getAll() {
        logger.info("Récupération de tous les signalements");
        List<SignalementResponseDTO> list = signalementService.getAll()
                .stream()
                .map(SignalementMapper::toDTO)
                .collect(Collectors.toList());
        logger.info("Nombre de signalements récupérés : {}", list.size());
        return ResponseEntity.ok(list);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> getById(@PathVariable Long id) {
        logger.info("Récupération du signalement ID : {}", id);
        SignalementResponseDTO response = SignalementMapper.toDTO(signalementService.getById(id));
        logger.info("Signalement trouvé : {}", response);
        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> traiter(@PathVariable Long id,
                                                          @RequestParam String statut,
                                                          @RequestParam String resolution) {
        logger.info("Traitement du signalement ID : {}, statut : {}, résolution : {}", id, statut, resolution);
        SignalementResponseDTO response = SignalementMapper.toDTO(
                signalementService.traiter(id, statut, resolution)
        );
        logger.info("Signalement traité : {}", response);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        logger.info("Suppression du signalement ID : {}", id);
        signalementService.supprimer(id);
        logger.info("Signalement supprimé avec succès");
        return ResponseEntity.noContent().build();
    }
}
