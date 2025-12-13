package com.example.backend.mappers;

import com.example.backend.dto.AvisDTO;
import com.example.backend.entities.Avis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvisMapper {
    public Avis toEntity(AvisDTO avisDTO);
    public AvisDTO toDTO(Avis avis);
}
