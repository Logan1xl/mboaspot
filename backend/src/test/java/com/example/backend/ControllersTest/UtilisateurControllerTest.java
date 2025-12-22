package com.example.backend.ControllersTest;

import com.example.backend.controllers.UtilisateurController;
import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.services.implementation.UtilisateurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@WebMvcTest(UtilisateurController.class)
@AutoConfigureMockMvc(addFilters = false)
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private UtilisateurDTO utilisateurDTO;

    @BeforeEach
    void setUp() {
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(1L);
        utilisateurDTO.setEmail("test@example.com");
        utilisateurDTO.setMotDePasse("password123");
        utilisateurDTO.setPrenom("Jean");
        utilisateurDTO.setNom("Dupont");
        utilisateurDTO.setNumeroTelephone("+237612345678");
        utilisateurDTO.setPhotoProfil("photo.jpg");
        utilisateurDTO.setEstActif(true);
        utilisateurDTO.setRole("VOYAGEUR");
    }


    @Test
    void getAllUtilisateurs_ShouldReturnEmptyList_WhenNoUtilisateurs() throws Exception {
        when(utilisateurService.getAllUtilisateurs()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(utilisateurService, times(1)).getAllUtilisateurs();
    }

    @Test
    void getUtilisateurById_ShouldReturnUtilisateur_WhenIdExists() throws Exception {
        when(utilisateurService.getUtilisateurById(1L)).thenReturn(utilisateurDTO);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.prenom").value("Jean"))
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.numeroTelephone").value("+237612345678"))
                .andExpect(jsonPath("$.estActif").value(true))
                .andExpect(jsonPath("$.role").value("VOYAGEUR"));

        verify(utilisateurService, times(1)).getUtilisateurById(1L);
    }

    @Test
    void getUtilisateurById_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        when(utilisateurService.getUtilisateurById(99L))
                .thenThrow(new RuntimeException("Utilisateur non trouvé"));

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Utilisateur non trouvé"));

        verify(utilisateurService, times(1)).getUtilisateurById(99L);
    }

    @Test
    void createUtilisateur_ShouldReturnCreated_WhenUtilisateurIsValid() throws Exception {
        when(utilisateurService.createUtilisateur(any(UtilisateurDTO.class))).thenReturn(utilisateurDTO);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.prenom").value("Jean"))
                .andExpect(jsonPath("$.nom").value("Dupont"));

        verify(utilisateurService, times(1)).createUtilisateur(any(UtilisateurDTO.class));
    }

    @Test
    void createUtilisateur_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(utilisateurService.createUtilisateur(any(UtilisateurDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la création"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la création"));

        verify(utilisateurService, times(1)).createUtilisateur(any(UtilisateurDTO.class));
    }

    @Test
    void updateUtilisateur_ShouldReturnUpdatedUtilisateur_WhenValid() throws Exception {
        utilisateurDTO.setPrenom("Pierre");
        utilisateurDTO.setNumeroTelephone("+237698765432");

        when(utilisateurService.updateUtilisateur(eq(1L), any(UtilisateurDTO.class))).thenReturn(utilisateurDTO);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.prenom").value("Pierre"))
                .andExpect(jsonPath("$.numeroTelephone").value("+237698765432"));

        verify(utilisateurService, times(1)).updateUtilisateur(eq(1L), any(UtilisateurDTO.class));
    }

    @Test
    void updateUtilisateur_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        when(utilisateurService.updateUtilisateur(eq(99L), any(UtilisateurDTO.class)))
                .thenThrow(new RuntimeException("Utilisateur non trouvé"));

        mockMvc.perform(put("/users/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Utilisateur non trouvé"));
    }
    @Test
    void getAllUtilisateurs_ShouldReturnListOfUtilisateurs() throws Exception {
        UtilisateurDTO utilisateur2 = new UtilisateurDTO();
        utilisateur2.setId(2L);
        utilisateur2.setEmail("marie@example.com");
        utilisateur2.setPrenom("Marie");
        utilisateur2.setNom("Martin");

        List<UtilisateurDTO> utilisateurs = Arrays.asList(utilisateurDTO, utilisateur2);
        when(utilisateurService.getAllUtilisateurs()).thenReturn(utilisateurs);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].prenom").value("Jean"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].email").value("marie@example.com"));

        verify(utilisateurService, times(1)).getAllUtilisateurs();
    }



    @Test
    void updateUtilisateur_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        when(utilisateurService.updateUtilisateur(eq(1L), any(UtilisateurDTO.class)))
                .thenThrow(new RuntimeException("Erreur lors de la mise à jour"));

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilisateurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la mise à jour"));
    }

    @Test
    void deleteUtilisateur_ShouldReturnSuccessMessage_WhenIdExists() throws Exception {
        doNothing().when(utilisateurService).deleteUtilisateur(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Utilisateur supprimé avec succès"));

        verify(utilisateurService, times(1)).deleteUtilisateur(1L);
    }

    @Test
    void deleteUtilisateur_ShouldReturnBadRequest_WhenIdDoesNotExist() throws Exception {
        doThrow(new RuntimeException("Utilisateur non trouvé"))
                .when(utilisateurService).deleteUtilisateur(99L);

        mockMvc.perform(delete("/users/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Utilisateur non trouvé"));

        verify(utilisateurService, times(1)).deleteUtilisateur(99L);
    }

    @Test
    void deleteUtilisateur_ShouldReturnBadRequest_WhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Impossible de supprimer cet utilisateur"))
                .when(utilisateurService).deleteUtilisateur(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Impossible de supprimer cet utilisateur"));
    }
}