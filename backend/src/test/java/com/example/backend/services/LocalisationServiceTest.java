package com.example.backend.services;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.entities.Localisation;
import com.example.backend.mappers.LocalisationMapper;
import com.example.backend.repositories.LocalisationRepository;
import com.example.backend.services.implementations.LocalisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocalisationServiceTest {

    @Mock
    private LocalisationRepository localisationRepository;

    @Mock
    private LocalisationMapper localisationMapper;

    @InjectMocks
    private LocalisationService localisationService;

    private Localisation localisation;
    private LocalisationDTO localisationDTO;

    @BeforeEach
    void setUp() {
        // Utilisation de .setVille() car .setNom() est vide dans ton entité
        localisation = new Localisation();
        localisation.setId(1L);
        localisation.setVille("Douala");

        // Utilisation de .setVille() car .getNom() renvoie null dans ton DTO
        localisationDTO = new LocalisationDTO();
        localisationDTO.setId(1L);
        localisationDTO.setVille("Douala");
    }

    @Test
    void save_ShouldReturnSavedDto() {
        // Arrange
        when(localisationMapper.toEntity(any(LocalisationDTO.class))).thenReturn(localisation);
        when(localisationRepository.save(any(Localisation.class))).thenReturn(localisation);
        when(localisationMapper.toDto(any(Localisation.class))).thenReturn(localisationDTO);

        // Act
        LocalisationDTO result = localisationService.save(localisationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Douala", result.getVille()); // Test sur la ville
        verify(localisationRepository, times(1)).save(any());
    }

    @Test
    void getAll_ShouldReturnListOfDtos() {
        // Arrange
        List<Localisation> localisations = Arrays.asList(localisation, new Localisation());
        when(localisationRepository.findAll()).thenReturn(localisations);
        when(localisationMapper.toDto(any(Localisation.class))).thenReturn(localisationDTO);

        // Act
        List<LocalisationDTO> result = localisationService.getAll();

        // Assert
        assertEquals(2, result.size());
        verify(localisationRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnDto_WhenIdExists() {
        // Arrange
        when(localisationRepository.findById(1L)).thenReturn(Optional.of(localisation));
        when(localisationMapper.toDto(localisation)).thenReturn(localisationDTO);

        // Act
        LocalisationDTO result = localisationService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Douala", result.getVille());
    }

    @Test
    void delete_ShouldCallRepository() {
        // Act
        localisationService.delete(1L);

        // Assert
        verify(localisationRepository, times(1)).deleteById(1L);
    }

    @Test
    void update_ShouldReturnUpdatedDto() {
        // Arrange
        when(localisationMapper.toEntity(any(LocalisationDTO.class))).thenReturn(localisation);
        when(localisationRepository.save(any(Localisation.class))).thenReturn(localisation);
        when(localisationMapper.toDto(any(Localisation.class))).thenReturn(localisationDTO);

        // Act
        LocalisationDTO result = localisationService.update(1L, localisationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Douala", result.getVille());
        verify(localisationRepository).save(any());
        assertEquals(1L, localisation.getId());
    }
}