package com.example.backend.auth.authController.reservationcontroller;


import com.example.backend.dto.reservationdto.AnnonceRequestDTO;
import com.example.backend.dto.reservationdto.AnnonceResponseDTO;
import com.example.backend.services.reservationservice.AnnonceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.default_catalog=",
        "jwt.secret=testSecretKeyForJwtThatIsAtLeast256BitsLongForHS256Algorithm",
        "jwt.expiration=86400000"
})
@ActiveProfiles("test")
class AnnonceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnnonceService annonceService;

    private AnnonceRequestDTO annonceRequestDTO;
    private AnnonceResponseDTO annonceResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuration du DTO de requête
        annonceRequestDTO = new AnnonceRequestDTO();
        annonceRequestDTO.setTitre("Belle maison à Douala");
        annonceRequestDTO.setPrix(50000.0);
        annonceRequestDTO.setAdresse("Akwa, Douala");
        annonceRequestDTO.setVille("Douala");
        annonceRequestDTO.setNbreChambres(3);
        annonceRequestDTO.setNbreLits(2);
        annonceRequestDTO.setMaxInvites(4);
        annonceRequestDTO.setDescription("Une magnifique maison au centre ville");
        annonceRequestDTO.setTypeAnnonce("MAISON");
        annonceRequestDTO.setProprietaireId(1L);

        // Configuration de la réponse
        annonceResponseDTO = new AnnonceResponseDTO();
        annonceResponseDTO.setId(1L);
        annonceResponseDTO.setTitre("Belle maison à Douala");
        annonceResponseDTO.setPrix(50000.0);
        annonceResponseDTO.setAdresse("Akwa, Douala");
        annonceResponseDTO.setVille("Douala");
        annonceResponseDTO.setNbreChambres(3);
        annonceResponseDTO.setNbreLits(2);
        annonceResponseDTO.setMaxInvites(4);
        annonceResponseDTO.setDescription("Une magnifique maison au centre ville");
        annonceResponseDTO.setTypeAnnonce("MAISON");
        annonceResponseDTO.setEstActive(true);
        annonceResponseDTO.setEvaluationMoyenne(0.0);
        annonceResponseDTO.setTotalAvis(0);
        annonceResponseDTO.setProprietaireId(1L);
        annonceResponseDTO.setProprietaireNom("Entreprise Test");
    }

    // ==================== TESTS CREER ANNONCE ====================

    @Test
    void creerAnnonce_AvecDonneesValides_DevraitRetourner200() throws Exception {
        // Arrange
        when(annonceService.creerAnnonce(any(AnnonceRequestDTO.class)))
                .thenReturn(annonceResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/annonces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Belle maison à Douala"))
                .andExpect(jsonPath("$.prix").value(50000.0))
                .andExpect(jsonPath("$.adresse").value("Akwa, Douala"))
                .andExpect(jsonPath("$.ville").value("Douala"))
                .andExpect(jsonPath("$.nbreChambres").value(3))
                .andExpect(jsonPath("$.nbreLits").value(2))
                .andExpect(jsonPath("$.maxInvites").value(4))
                .andExpect(jsonPath("$.description").value("Une magnifique maison au centre ville"))
                .andExpect(jsonPath("$.typeAnnonce").value("MAISON"))
                .andExpect(jsonPath("$.estActive").value(true))
                .andExpect(jsonPath("$.proprietaireId").value(1))
                .andExpect(jsonPath("$.proprietaireNom").value("Entreprise Test"));

        verify(annonceService, times(1)).creerAnnonce(any(AnnonceRequestDTO.class));
    }

    @Test
    void creerAnnonce_AvecProprietaireInexistant_DevraitRetourner500() throws Exception {
        // Arrange
        when(annonceService.creerAnnonce(any(AnnonceRequestDTO.class)))
                .thenThrow(new RuntimeException("Propriétaire non trouvé"));

        // Act & Assert
        mockMvc.perform(post("/api/annonces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Propriétaire non trouvé"));

        verify(annonceService, times(1)).creerAnnonce(any(AnnonceRequestDTO.class));
    }

    // ==================== TESTS OBTENIR ANNONCE ====================

    @Test
    void obtenirAnnonce_AvecIdValide_DevraitRetourner200() throws Exception {
        // Arrange
        when(annonceService.obtenirAnnonce(1L)).thenReturn(annonceResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/annonces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Belle maison à Douala"));

        verify(annonceService, times(1)).obtenirAnnonce(1L);
    }

    @Test
    void obtenirAnnonce_AvecIdInexistant_DevraitRetourner500() throws Exception {
        // Arrange
        when(annonceService.obtenirAnnonce(anyLong()))
                .thenThrow(new RuntimeException("Annonce non trouvée"));

        // Act & Assert
        mockMvc.perform(get("/api/annonces/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Annonce non trouvée"));

        verify(annonceService, times(1)).obtenirAnnonce(999L);
    }

    // ==================== TESTS OBTENIR ANNONCES ACTIVES ====================

    @Test
    void obtenirAnnoncesActives_AvecAnnoncesDisponibles_DevraitRetourner200() throws Exception {
        // Arrange
        List<AnnonceResponseDTO> annonces = Arrays.asList(annonceResponseDTO);
        when(annonceService.obtenirAnnoncesActives()).thenReturn(annonces);

        // Act & Assert
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titre").value("Belle maison à Douala"))
                .andExpect(jsonPath("$[0].estActive").value(true));

        verify(annonceService, times(1)).obtenirAnnoncesActives();
    }

    @Test
    void obtenirAnnoncesActives_SansAnnonces_DevraitRetourner200ListeVide() throws Exception {
        // Arrange
        when(annonceService.obtenirAnnoncesActives()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(annonceService, times(1)).obtenirAnnoncesActives();
    }

    // ==================== TESTS OBTENIR ANNONCES PROPRIETAIRE ====================

    @Test
    void obtenirAnnoncesProprietaire_AvecProprietaireAyantAnnonces_DevraitRetourner200() throws Exception {
        // Arrange
        List<AnnonceResponseDTO> annonces = Arrays.asList(annonceResponseDTO);
        when(annonceService.obtenirAnnoncesProprietaire(1L)).thenReturn(annonces);

        // Act & Assert
        mockMvc.perform(get("/api/annonces/proprietaire/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].proprietaireId").value(1));

        verify(annonceService, times(1)).obtenirAnnoncesProprietaire(1L);
    }

    @Test
    void obtenirAnnoncesProprietaire_AvecProprietaireSansAnnonces_DevraitRetourner200ListeVide() throws Exception {
        // Arrange
        when(annonceService.obtenirAnnoncesProprietaire(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/annonces/proprietaire/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(annonceService, times(1)).obtenirAnnoncesProprietaire(1L);
    }

    // ==================== TESTS METTRE A JOUR ANNONCE ====================

    @Test
    void mettreAJourAnnonce_AvecDonneesValides_DevraitRetourner200() throws Exception {
        // Arrange
        AnnonceRequestDTO updateDTO = new AnnonceRequestDTO();
        updateDTO.setTitre("Nouvelle maison");
        updateDTO.setPrix(60000.0);
        updateDTO.setAdresse("Bonanjo, Douala");
        updateDTO.setVille("Douala");
        updateDTO.setNbreChambres(4);
        updateDTO.setNbreLits(3);
        updateDTO.setMaxInvites(6);
        updateDTO.setDescription("Description mise à jour");
        updateDTO.setTypeAnnonce("APPARTEMENT");
        updateDTO.setProprietaireId(1L);

        AnnonceResponseDTO updatedResponse = new AnnonceResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setTitre("Nouvelle maison");
        updatedResponse.setPrix(60000.0);
        updatedResponse.setAdresse("Bonanjo, Douala");

        when(annonceService.mettreAJourAnnonce(eq(1L), any(AnnonceRequestDTO.class)))
                .thenReturn(updatedResponse);

        // Act & Assert
        mockMvc.perform(put("/api/annonces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titre").value("Nouvelle maison"))
                .andExpect(jsonPath("$.prix").value(60000.0));

        verify(annonceService, times(1)).mettreAJourAnnonce(eq(1L), any(AnnonceRequestDTO.class));
    }

    @Test
    void mettreAJourAnnonce_AvecAnnonceInexistante_DevraitRetourner500() throws Exception {
        // Arrange
        when(annonceService.mettreAJourAnnonce(anyLong(), any(AnnonceRequestDTO.class)))
                .thenThrow(new RuntimeException("Annonce non trouvée"));

        // Act & Assert
        mockMvc.perform(put("/api/annonces/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annonceRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Annonce non trouvée"));

        verify(annonceService, times(1)).mettreAJourAnnonce(eq(999L), any(AnnonceRequestDTO.class));
    }

    // ==================== TESTS CHANGER STATUT ANNONCE ====================

    @Test
    void changerStatutAnnonce_DevraitActiverAnnonce() throws Exception {
        // Arrange
        annonceResponseDTO.setEstActive(true);
        when(annonceService.changerStatutAnnonce(1L, true)).thenReturn(annonceResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/annonces/1/statut")
                        .param("estActive", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estActive").value(true));

        verify(annonceService, times(1)).changerStatutAnnonce(1L, true);
    }

    @Test
    void changerStatutAnnonce_DevraitDesactiverAnnonce() throws Exception {
        // Arrange
        annonceResponseDTO.setEstActive(false);
        when(annonceService.changerStatutAnnonce(1L, false)).thenReturn(annonceResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/annonces/1/statut")
                        .param("estActive", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estActive").value(false));

        verify(annonceService, times(1)).changerStatutAnnonce(1L, false);
    }

    @Test
    void changerStatutAnnonce_AvecAnnonceInexistante_DevraitRetourner500() throws Exception {
        // Arrange
        when(annonceService.changerStatutAnnonce(anyLong(), any(Boolean.class)))
                .thenThrow(new RuntimeException("Annonce non trouvée"));

        // Act & Assert
        mockMvc.perform(put("/api/annonces/999/statut")
                        .param("estActive", "true"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Annonce non trouvée"));

        verify(annonceService, times(1)).changerStatutAnnonce(999L, true);
    }

    // ==================== TESTS SUPPRIMER ANNONCE ====================

    @Test
    void supprimerAnnonce_AvecAnnonceValide_DevraitRetourner204() throws Exception {
        // Arrange
        doNothing().when(annonceService).supprimerAnnonce(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/annonces/1"))
                .andExpect(status().isNoContent());

        verify(annonceService, times(1)).supprimerAnnonce(1L);
    }

    @Test
    void supprimerAnnonce_AvecAnnonceInexistante_DevraitRetourner500() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Annonce non trouvée"))
                .when(annonceService).supprimerAnnonce(anyLong());

        // Act & Assert
        mockMvc.perform(delete("/api/annonces/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Annonce non trouvée"));

        verify(annonceService, times(1)).supprimerAnnonce(999L);
    }
}