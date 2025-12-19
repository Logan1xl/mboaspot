package com.example.backend.mappers;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.entities.Equipement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface EquipementMapper {

    Equipement toEntity(EquipementDTO equipementDTO);

    EquipementDTO toDto(Equipement equipement);

    List<EquipementDTO> toDto(List<Equipement> equipements);
}