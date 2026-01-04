package com.example.backend.mappers;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Favori;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriMapper {
    public Favori toEntity(FavoriDTO favoriDTO);
    public FavoriDTO toDTO(Favori favori);
}
