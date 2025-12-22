package com.example.backend.ControllersTest;

import com.example.backend.controllers.AvisController;
import com.example.backend.dto.AvisDTO;
import com.example.backend.entities.Voyageur;
import com.example.backend.services.implementation.AvisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvisController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AvisService avisService;

    private AvisDTO avisDTO;
    private Voyageur voyageur;

    @BeforeEach
    void setUp() {
        voyageur = new Voyageur();
        voyageur.setId(1L);

        avisDTO = new AvisDTO();
        avisDTO.setId(1L);
        avisDTO.setNote(5);
        avisDTO.setPhotos("photo1.jpg");
        avisDTO.setEstSignale(false);
        avisDTO.setRaisonSignalement(null);
        avisDTO.setIdVoyageur(voyageur);
    }



    @Test
    void createAvis_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(avisService.ajouterAvis(any(AvisDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la création"));

        mockMvc.perform(post("/api/avis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la création"));
    }

    @Test
    void getAllAvis_ShouldReturnListOfAvis() throws Exception {
        AvisDTO avisDTO2 = new AvisDTO();
        avisDTO2.setId(2L);
        avisDTO2.setNote(4);

        List<AvisDTO> avisList = Arrays.asList(avisDTO, avisDTO2);
        when(avisService.listerAvis()).thenReturn(avisList);

        mockMvc.perform(get("/api/avis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].note").value(5))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].note").value(4));

        verify(avisService, times(1)).listerAvis();
    }

    @Test
    void getAllAvis_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(avisService.listerAvis())
                .thenThrow(new RuntimeException("Erreur lors de la récupération"));

        mockMvc.perform(get("/api/avis"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la récupération"));
    }
    @Test
    void createAvis_ShouldReturnCreated_WhenAvisIsValid() throws Exception {
        when(avisService.ajouterAvis(any(AvisDTO.class))).thenReturn(avisDTO);

        mockMvc.perform(post("/api/avis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.note").value(5))
                .andExpect(jsonPath("$.estSignale").value(false));

        verify(avisService, times(1)).ajouterAvis(any(AvisDTO.class));
    }

   /* @Test
    void getAvisById_ShouldReturnAvis_WhenIdExists() throws Exception {
        when(avisService.obtenirAvisParId(1L)).thenReturn(avisDTO);

        mockMvc.perform(get("/api/avis/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.note").value(5));

        verify(avisService, times(1)).obtenirAvisParId(1L);
    }*/

   /* @Test
    void getAvisById_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        when(avisService.obtenirAvisParId(99L))
                .thenThrow(new RuntimeException("Avis non trouvé"));

        mockMvc.perform(get("/api/avis/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Avis non trouvé"));
    }*/

    @Test
    void updateAvis_ShouldReturnUpdatedAvis_WhenValid() throws Exception {
        avisDTO.setNote(3);
        when(avisService.mettreAJourAvis(eq(1L), any(AvisDTO.class))).thenReturn(avisDTO);

        mockMvc.perform(put("/api/avis/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.note").value(3));

        verify(avisService, times(1)).mettreAJourAvis(eq(1L), any(AvisDTO.class));
    }

    @Test
    void updateAvis_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(avisService.mettreAJourAvis(eq(99L), any(AvisDTO.class)))
                .thenThrow(new RuntimeException("Avis non trouvé"));

        mockMvc.perform(put("/api/avis/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Avis non trouvé"));
    }

    @Test
    void deleteAvis_ShouldReturnSuccessMessage_WhenIdExists() throws Exception {
        doNothing().when(avisService).supprimerAvis(1L);

        mockMvc.perform(delete("/api/avis/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Avis supprimé avec succès"));

        verify(avisService, times(1)).supprimerAvis(1L);
    }

    @Test
    void deleteAvis_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Avis non trouvé"))
                .when(avisService).supprimerAvis(99L);

        mockMvc.perform(delete("/api/avis/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Avis non trouvé"));
    }
}