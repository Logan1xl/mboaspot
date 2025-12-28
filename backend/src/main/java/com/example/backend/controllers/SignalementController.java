package com.example.backend.controllers;

import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.dto.SignalementResponseDTO;
import com.example.backend.entities.Signalement;
import com.example.backend.mappers.SignalementMapper;
import com.example.backend.services.implementations.SignalementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/signalements")
@CrossOrigin
public class SignalementController {

    private final SignalementService signalementService;

    public SignalementController(SignalementService signalementService) {
        this.signalementService = signalementService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<SignalementResponseDTO> creer(
            @RequestBody SignalementRequestDTO dto) {

        Signalement s = signalementService.creer(dto);
        return ResponseEntity.ok(SignalementMapper.toDTO(s));
    }



    // READ ALL
    @GetMapping
    public ResponseEntity<List<SignalementResponseDTO>> getAll() {
        return ResponseEntity.ok(
                signalementService.getAll()
                        .stream()
                        .map(SignalementMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                SignalementMapper.toDTO(signalementService.getById(id)));

    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SignalementResponseDTO> traiter(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam String resolution) {

        return ResponseEntity.ok(
                SignalementMapper.toDTO(
                        signalementService.traiter(id, statut, resolution)
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        signalementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
