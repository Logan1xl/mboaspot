package com.example.backend.controllers;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.services.implementations.EquipementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EquipementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // Utilise @MockBean si tu es sur une version Spring Boot < 3.4
    private EquipementService equipementService;

    // --- TEST CREATE (POST) ---
    @Test
    void testSave() throws Exception {
        EquipementDTO dto = new EquipementDTO();
        dto.setId(1L);
        dto.setNom("Ordinateur");

        Mockito.when(equipementService.save(any(EquipementDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/equipement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Ordinateur"));
    }

    // --- TEST GET BY ID ---
    @Test
    void testGetById_Success() throws Exception {
        EquipementDTO dto = new EquipementDTO();
        dto.setId(1L);
        dto.setNom("Scanner");

        Mockito.when(equipementService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/equipement/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Scanner"));
    }

    // --- TEST GET ALL ---
    @Test
    void testGetAll() throws Exception {
        EquipementDTO e1 = new EquipementDTO();
        EquipementDTO e2 = new EquipementDTO();
        Mockito.when(equipementService.getAll()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/equipement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --- TEST UPDATE (PUT) ---
    @Test
    void testUpdate() throws Exception {
        EquipementDTO updatedDto = new EquipementDTO();
        updatedDto.setNom("Nouveau Nom");

        Mockito.when(equipementService.update(eq(1L), any(EquipementDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/equipement/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Nouveau Nom"));
    }

    // --- TEST DELETE ---
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/equipement/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Équipement supprimé avec succès"));

        Mockito.verify(equipementService).deleteById(1L);
    }
}