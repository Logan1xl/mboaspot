package com.example.backend.ControllersTest;

import com.example.backend.controllers.FavoriController;
import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Voyageur;
import com.example.backend.services.implementation.FavoriService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavoriService favoriService;

    private FavoriDTO favoriDTO;
    private Voyageur voyageur;
    private Annonces annonce;

    @BeforeEach
    void setUp() {
        voyageur = new Voyageur();
        voyageur.setId(1L);

        annonce = new Annonces();
        annonce.setId(1L);

        favoriDTO = new FavoriDTO();
        favoriDTO.setId(1L);
        favoriDTO.setIdVoyageur(voyageur);
        favoriDTO.setIdAnnonce(annonce);
    }

    @Test
    void getFavorisByUserId_ShouldReturnFavori_WhenIdExists() throws Exception {
        when(favoriService.obtenirFavoriParId(1L)).thenReturn(favoriDTO);

        mockMvc.perform(get("/api/favori/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idVoyageur.id").value(1L))
                .andExpect(jsonPath("$.idAnnonce.id").value(1L));

        verify(favoriService, times(1)).obtenirFavoriParId(1L);
    }

    @Test
    void getFavorisByUserId_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        when(favoriService.obtenirFavoriParId(99L))
                .thenThrow(new RuntimeException("Favori non trouvé"));

        mockMvc.perform(get("/api/favori/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Favori non trouvé"));

        verify(favoriService, times(1)).obtenirFavoriParId(99L);
    }

    @Test
    void createFavori_ShouldReturnCreated_WhenFavoriIsValid() throws Exception {
        when(favoriService.ajouterFavori(any(FavoriDTO.class))).thenReturn(favoriDTO);

        mockMvc.perform(post("/api/favori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idVoyageur.id").value(1L))
                .andExpect(jsonPath("$.idAnnonce.id").value(1L));

        verify(favoriService, times(1)).ajouterFavori(any(FavoriDTO.class));
    }

    @Test
    void createFavori_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(favoriService.ajouterFavori(any(FavoriDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la création du favori"));

        mockMvc.perform(post("/api/favori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la création du favori"));
    }

    @Test
    void createFavori_ShouldReturnBadRequest_WhenFavoriAlreadyExists() throws Exception {
        when(favoriService.ajouterFavori(any(FavoriDTO.class)))
                .thenThrow(new RuntimeException("Ce favori existe déjà"));

        mockMvc.perform(post("/api/favori")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ce favori existe déjà"));
    }

    @Test
    void updateFavori_ShouldReturnUpdatedFavori_WhenValid() throws Exception {
        Annonces nouvelleAnnonce = new Annonces();
        nouvelleAnnonce.setId(2L);
        favoriDTO.setIdAnnonce(nouvelleAnnonce);

        when(favoriService.mettreAJourFavori(eq(1L), any(FavoriDTO.class))).thenReturn(favoriDTO);

        mockMvc.perform(put("/api/favori/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idAnnonce.id").value(2L));

        verify(favoriService, times(1)).mettreAJourFavori(eq(1L), any(FavoriDTO.class));
    }

    @Test
    void updateFavori_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        when(favoriService.mettreAJourFavori(eq(99L), any(FavoriDTO.class)))
                .thenThrow(new RuntimeException("Favori non trouvé"));

        mockMvc.perform(put("/api/favori/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Favori non trouvé"));
    }

    @Test
    void updateFavori_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(favoriService.mettreAJourFavori(eq(1L), any(FavoriDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la mise à jour"));

        mockMvc.perform(put("/api/favori/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(favoriDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la mise à jour"));
    }

    @Test
    void deleteFavori_ShouldReturnSuccessMessage_WhenIdExists() throws Exception {
        doNothing().when(favoriService).supprimerFavori(1L);

        mockMvc.perform(delete("/api/favori/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Favori supprimé avec succès"));

        verify(favoriService, times(1)).supprimerFavori(1L);
    }

    @Test
    void deleteFavori_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        doThrow(new RuntimeException("Favori non trouvé"))
                .when(favoriService).supprimerFavori(99L);

        mockMvc.perform(delete("/api/favori/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Favori non trouvé"));

        verify(favoriService, times(1)).supprimerFavori(99L);
    }

    @Test
    void deleteFavori_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Erreur lors de la suppression"))
                .when(favoriService).supprimerFavori(1L);

        mockMvc.perform(delete("/api/favori/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la suppression"));
    }
}