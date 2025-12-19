package com.example.backend.mappers;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.entities.Localisation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocalisationMapper {

    Localisation toEntity(LocalisationDTO localisationDTO);

    LocalisationDTO toDto(Localisation localisation);

    /*List<LocalisationDTO> toDto(List<Localisation> localisations);*/
}
