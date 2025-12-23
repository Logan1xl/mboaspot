package com.example.backend.controller;

import com.example.backend.controllers.LocalisationController;
import com.example.backend.dto.LocalisationDTO;
import com.example.backend.services.implementations.LocalisationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocalisationController.class)
class LocalisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocalisationService localisationService;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalisationDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new LocalisationDTO();
        sampleDto.setId(1L);
        sampleDto.setVille("Douala");
        sampleDto.setQuartier("Akwa");
        // On ne teste pas "nom" car il renvoie null dans ton DTO
    }

    // 1. TEST CRÉER (POST)
    @Test
    void testAddLocalisation() throws Exception {
        when(localisationService.save(any(LocalisationDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(post("/localisation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ville").value("Douala"))
                .andExpect(jsonPath("$.quartier").value("Akwa"));
    }

    // 2. TEST RÉCUPÉRER TOUT (GET ALL)
    @Test
    void testGetAllLocalisation() throws Exception {
        LocalisationDTO loc2 = new LocalisationDTO();
        loc2.setVille("Yaoundé");
        List<LocalisationDTO> all = Arrays.asList(sampleDto, loc2);

        when(localisationService.getAll()).thenReturn(all);

        mockMvc.perform(get("/localisation/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].ville").value("Douala"))
                .andExpect(jsonPath("$[1].ville").value("Yaoundé"));
    }

    // 3. TEST RÉCUPÉRER PAR ID (GET BY ID)
    @Test
    void testGetLocalisationById_Success() throws Exception {
        when(localisationService.getById(1L)).thenReturn(sampleDto);

        mockMvc.perform(get("/localisation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ville").value("Douala"));
    }

    // 4. TEST METTRE À JOUR (PUT)
    @Test
    void testUpdateLocalisation() throws Exception {
        when(localisationService.update(eq(1L), any(LocalisationDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(put("/localisation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ville").value("Douala"));
    }

    // 5. TEST SUPPRIMER (DELETE)
    @Test
    void testDeleteLocalisation() throws Exception {
        // Pour une méthode void, on utilise doNothing()
        doNothing().when(localisationService).delete(1L);

        mockMvc.perform(delete("/localisation/1"))
                .andExpect(status().isNoContent());
    }
}