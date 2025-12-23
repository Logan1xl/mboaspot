package com.example.backend.service;



import com.example.backend.dto.EquipementDTO;
import com.example.backend.entities.Equipement;
import com.example.backend.mappers.EquipementMapper;
import com.example.backend.repositories.EquipementRepository;
import com.example.backend.services.implementations.EquipementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class EquipementServiceTest {

        @Mock
        private EquipementRepository equipementRepository;

        @Mock
        private EquipementMapper equipementMapper;

        @InjectMocks
        private EquipementService equipementService;

        private Equipement equipement;
        private EquipementDTO equipementDTO;

        @BeforeEach
        void setUp() {
            equipement = new Equipement();
            equipement.setId(1L);
            equipement.setNom("Projecteur");

            equipementDTO = new EquipementDTO();
            equipementDTO.setId(1L);
            equipementDTO.setNom("Projecteur");
        }

        @Test
        void testSave() {
            // GIVEN
            when(equipementMapper.toEntity(any(EquipementDTO.class))).thenReturn(equipement);
            when(equipementRepository.save(any(Equipement.class))).thenReturn(equipement);
            when(equipementMapper.toDto(any(Equipement.class))).thenReturn(equipementDTO);

            // WHEN
            EquipementDTO result = equipementService.save(equipementDTO);

            // THEN
            assertThat(result).isNotNull();
            assertThat(result.getNom()).isEqualTo("Projecteur");
            verify(equipementRepository, times(1)).save(any());
        }

        @Test
        void testGetById() {
            // GIVEN
            when(equipementRepository.findById(1L)).thenReturn(Optional.of(equipement));
            when(equipementMapper.toDto(equipement)).thenReturn(equipementDTO);

            // WHEN
            EquipementDTO result = equipementService.getById(1L);

            // THEN
            assertThat(result.getId()).isEqualTo(1L);
            verify(equipementRepository).findById(1L);
        }

        @Test
        void testGetAll() {
            // GIVEN
            List<Equipement> list = Arrays.asList(equipement, new Equipement());
            when(equipementRepository.findAll()).thenReturn(list);
            when(equipementMapper.toDto(any(Equipement.class))).thenReturn(equipementDTO);

            // WHEN
            List<EquipementDTO> result = equipementService.getAll();

            // THEN
            assertThat(result).hasSize(2);
            verify(equipementRepository).findAll();
        }

        @Test
        void testUpdate() {
            // GIVEN
            EquipementDTO updateInput = new EquipementDTO();
            updateInput.setNom("Nouveau Nom");

            when(equipementMapper.toEntity(any(EquipementDTO.class))).thenReturn(equipement);
            when(equipementRepository.save(any(Equipement.class))).thenReturn(equipement);
            when(equipementMapper.toDto(any(Equipement.class))).thenReturn(equipementDTO);

            // WHEN
            EquipementDTO result = equipementService.update(1L, updateInput);

            // THEN
            assertThat(result).isNotNull();
            verify(equipementRepository).save(any());
        }

        @Test
        void testDeleteById() {
            // WHEN
            equipementService.deleteById(1L);

            // THEN
            verify(equipementRepository, times(1)).deleteById(1L);
        }
    }

