package com.example.backend.mappers;

import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.dto.SignalementResponseDTO;
import com.example.backend.entities.Signalement;

public class SignalementMapper {

    public static Signalement toEntity(SignalementRequestDTO dto) {
        Signalement s = new Signalement();
        s.setRaison(dto.getRaison());
        s.setDescription(dto.getDescription());
        return s;
    }

    public static SignalementResponseDTO toDTO(Signalement s) {
        SignalementResponseDTO dto = new SignalementResponseDTO();
        dto.setId(s.getId());
        dto.setRaison(s.getRaison());
        dto.setDescription(s.getDescription());
        dto.setStatut(s.getStatut());
        dto.setResolution(s.getResolution()); // ✅ FIX MANQUANT
        dto.setAnnonceId(s.getIdAnnonce().getId());
        if (s.getIdAdmin() != null) {
            dto.setAdminId(s.getIdAdmin().getId());
        }
        return dto;
    }


}
