package com.example.backend.mappers;

import com.example.backend.dto.AnnoncesDTO;
import com.example.backend.entities.Annonces;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnnoncesMapper {
    Annonces toEntity(AnnoncesDTO annoncesDTO);
    AnnoncesDTO toDTO(Annonces annonces);
}
