
package com.example.backend.controllers;

import com.example.backend.dto.*;
import com.example.backend.services.implementations.AnnonceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnnonceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnnonceService annonceService;

    private AnnonceRequestDTO annonceRequestDTO;
    private AnnonceResponseDTO annonceResponseDTO;
    private AnnonceDTO annonceDTO;

    @BeforeEach
    void setUp() {
        annonceRequestDTO = new AnnonceRequestDTO();
        annonceRequestDTO.setTitre("Belle Villa");
        annonceRequestDTO.setPrix(150.0);
        annonceRequestDTO.setVille("Paris");
        annonceRequestDTO.setAdresse("123 rue Test");
        annonceRequestDTO.setNbreChambres(3);
        annonceRequestDTO.setNbreLits(4);
        annonceRequestDTO.setMaxInvites(6);
        annonceRequestDTO.setDescription("Description test");
        annonceRequestDTO.setTypeAnnonce("MAISON");
        annonceRequestDTO.setProprietaireId(1L);

        annonceResponseDTO = new AnnonceResponseDTO();
        annonceResponseDTO.setId(1L);
        annonceResponseDTO.setTitre("Belle Villa");
        annonceResponseDTO.setPrix(150.0);
        annonceResponseDTO.setVille("Paris");
        annonceResponseDTO.setEstActive(true);

        annonceDTO = new AnnonceDTO();
        annonceDTO.setId(1L);
        annonceDTO.setTitre("Belle Villa");
        annonceDTO.setPrix(150.0);
    }

    @Test
    @WithMockUser(roles = {"PROPRIETAIRE"})
    void creerAnnonce_Success() throws Exception {
        when(annonceService.creerAnnonce(any(AnnonceRequestDTO.class)))
                .thenReturn(annonceResponseDTO);

        mockMvc.perform(post("/api/annonces/v1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("Belle Villa"))
                .andExpect(jsonPath("$.prix").value(150.0));

        verify(annonceService, times(1)).creerAnnonce(any(AnnonceRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = {"PROPRIETAIRE"})
    void creerAnnonce_BadRequest() throws Exception {
        when(annonceService.creerAnnonce(any(AnnonceRequestDTO.class)))
                .thenThrow(new RuntimeException("Propriétaire non trouvé"));

        mockMvc.perform(post("/api/annonces/v1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceRequestDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void obtenirAnnonce_Success() throws Exception {
        when(annonceService.obtenirAnnonce(1L)).thenReturn(annonceResponseDTO);

        mockMvc.perform(get("/api/annonces/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titre").value("Belle Villa"));

        verify(annonceService, times(1)).obtenirAnnonce(1L);
    }

    @Test
    @WithMockUser
    void getAllAnnonces_Success() throws Exception {
        when(annonceService.getAllAnnonces())
                .thenReturn(Arrays.asList(annonceDTO));

        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].titre").value("Belle Villa"));

        verify(annonceService, times(1)).getAllAnnonces();
    }

    @Test
    @WithMockUser
    void getAnnonceById_Success() throws Exception {
        when(annonceService.getAnnonceById(1L)).thenReturn(Optional.of(annonceDTO));

        mockMvc.perform(get("/api/annonces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(annonceService, times(1)).getAnnonceById(1L);
    }

    @Test
    @WithMockUser
    void getAnnonceById_NotFound() throws Exception {
        when(annonceService.getAnnonceById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/annonces/1"))
                .andExpect(status().isNotFound());

        verify(annonceService, times(1)).getAnnonceById(1L);
    }

    @Test
    @WithMockUser(roles = {"PROPRIETAIRE"})
    void updateAnnonce_Success() throws Exception {
        when(annonceService.updateAnnonce(eq(1L), any(AnnonceDTO.class)))
                .thenReturn(annonceDTO);

        mockMvc.perform(put("/api/annonces/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(annonceService, times(1)).updateAnnonce(eq(1L), any(AnnonceDTO.class));
    }

    @Test
    @WithMockUser(roles = {"PROPRIETAIRE"})
    void deleteAnnonce_Success() throws Exception {
        doNothing().when(annonceService).deleteAnnonce(1L);

        mockMvc.perform(delete("/api/annonces/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(annonceService, times(1)).deleteAnnonce(1L);
    }

    @Test
    @WithMockUser(roles = {"PROPRIETAIRE"})
    void changerStatutAnnonce_Success() throws Exception {
        when(annonceService.changerStatutAnnonce(1L, false))
                .thenReturn(annonceResponseDTO);

        mockMvc.perform(put("/api/annonces/v1/1/statut")
                        .with(csrf())
                        .param("estActive", "false"))
                .andExpect(status().isOk());

        verify(annonceService, times(1)).changerStatutAnnonce(1L, false);
    }
}