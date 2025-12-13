package com.example.backend.mappers;

import com.example.backend.dto.VoyageurDTO;
import com.example.backend.entities.Voyageur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoyageurMapper {
    Voyageur toVoyageur(VoyageurDTO voyageurDTO);
    VoyageurDTO toDTO(Voyageur voyageur);
}
