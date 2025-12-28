package com.example.backend.controllers;


import com.example.backend.dto.DisponibiliteRequestDTO;
import com.example.backend.dto.ReservationRequestDTO;
import com.example.backend.dto.ReservationResponseDTO;
import com.example.backend.services.implementations.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    private DisponibiliteRequestDTO disponibiliteRequestDTO;
    private ReservationRequestDTO reservationRequestDTO;
    private ReservationResponseDTO reservationResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuration du DTO de disponibilité
        disponibiliteRequestDTO = new DisponibiliteRequestDTO();
        disponibiliteRequestDTO.setAnnonceId(1L);
        disponibiliteRequestDTO.setDateDebut(LocalDate.now().plusDays(1));
        disponibiliteRequestDTO.setDateFin(LocalDate.now().plusDays(3));
        disponibiliteRequestDTO.setNombreInvites(2);

        // Configuration du DTO de requête de réservation
        reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setAnnonceId(1L);
        reservationRequestDTO.setVoyageurId(1L);
        reservationRequestDTO.setDateDebut(LocalDate.now().plusDays(1));
        reservationRequestDTO.setDateFin(LocalDate.now().plusDays(3));
        reservationRequestDTO.setNombreInvites(2);

        // Configuration de la réponse
        reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(1L);
        reservationResponseDTO.setCodeConfirmation("RES-12345678");
        reservationResponseDTO.setNombreInvites(2);
        reservationResponseDTO.setPrixTotal(100000.0);
        reservationResponseDTO.setStatut("EN_ATTENTE");
        reservationResponseDTO.setAnnonceId(1L);
        reservationResponseDTO.setAnnonceTitre("Belle maison à Douala");
        reservationResponseDTO.setAnnonceVille("Douala");
        reservationResponseDTO.setVoyageurId(1L);
    }

    // ==================== TESTS VERIFIER DISPONIBILITE ====================

    @Test
    void verifierDisponibilite_AvecAnnonceDisponible_DevraitRetourner200True() throws Exception {
        // Arrange
        when(reservationService.verifierDisponibilite(any(DisponibiliteRequestDTO.class)))
                .thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/reservations/disponibilite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disponibiliteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(reservationService, times(1)).verifierDisponibilite(any(DisponibiliteRequestDTO.class));
    }

    @Test
    void verifierDisponibilite_AvecAnnonceNonDisponible_DevraitRetourner200False() throws Exception {
        // Arrange
        when(reservationService.verifierDisponibilite(any(DisponibiliteRequestDTO.class)))
                .thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/reservations/disponibilite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disponibiliteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(reservationService, times(1)).verifierDisponibilite(any(DisponibiliteRequestDTO.class));
    }

    // ==================== TESTS CREER RESERVATION ====================

    @Test
    void creerReservation_AvecDonneesValides_DevraitRetourner200() throws Exception {
        // Arrange
        when(reservationService.creerReservation(any(ReservationRequestDTO.class)))
                .thenReturn(reservationResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codeConfirmation").value("RES-12345678"))
                .andExpect(jsonPath("$.nombreInvites").value(2))
                .andExpect(jsonPath("$.prixTotal").value(100000.0))
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"))
                .andExpect(jsonPath("$.annonceId").value(1))
                .andExpect(jsonPath("$.annonceTitre").value("Belle maison à Douala"))
                .andExpect(jsonPath("$.annonceVille").value("Douala"))
                .andExpect(jsonPath("$.voyageurId").value(1));

        verify(reservationService, times(1)).creerReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void creerReservation_AvecAnnonceNonDisponible_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.creerReservation(any(ReservationRequestDTO.class)))
                .thenThrow(new RuntimeException("anonces non disponible"));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("anonces non disponible"));

        verify(reservationService, times(1)).creerReservation(any(ReservationRequestDTO.class));
    }

    @Test
    void creerReservation_AvecAnnonceInexistante_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.creerReservation(any(ReservationRequestDTO.class)))
                .thenThrow(new RuntimeException("Annonce non trouvée"));

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Annonce non trouvée"));

        verify(reservationService, times(1)).creerReservation(any(ReservationRequestDTO.class));
    }

    // ==================== TESTS OBTENIR RESERVATION ====================

    @Test
    void obtenirReservation_AvecIdValide_DevraitRetourner200() throws Exception {
        // Arrange
        when(reservationService.obtenirReservation(1L)).thenReturn(reservationResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codeConfirmation").value("RES-12345678"));

        verify(reservationService, times(1)).obtenirReservation(1L);
    }

    @Test
    void obtenirReservation_AvecIdInexistant_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.obtenirReservation(anyLong()))
                .thenThrow(new RuntimeException("Réservation non trouvée"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Réservation non trouvée"));

        verify(reservationService, times(1)).obtenirReservation(999L);
    }

    // ==================== TESTS OBTENIR RESERVATIONS VOYAGEUR ====================

    @Test
    void obtenirReservationsVoyageur_AvecVoyageurAyantReservations_DevraitRetourner200() throws Exception {
        // Arrange
        List<ReservationResponseDTO> reservations = Arrays.asList(reservationResponseDTO);
        when(reservationService.obtenirReservationsVoyageur(1L)).thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/voyageur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].voyageurId").value(1));

        verify(reservationService, times(1)).obtenirReservationsVoyageur(1L);
    }

    @Test
    void obtenirReservationsVoyageur_AvecVoyageurSansReservations_DevraitRetourner200ListeVide() throws Exception {
        // Arrange
        when(reservationService.obtenirReservationsVoyageur(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/reservations/voyageur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(reservationService, times(1)).obtenirReservationsVoyageur(1L);
    }

    // ==================== TESTS ANNULER RESERVATION ====================

    @Test
    void annulerReservation_AvecReservationValide_DevraitRetourner200() throws Exception {
        // Arrange
        reservationResponseDTO.setStatut("ANNULEE");
        when(reservationService.annulerReservation(1L)).thenReturn(reservationResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1/annuler"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("ANNULEE"));

        verify(reservationService, times(1)).annulerReservation(1L);
    }

    @Test
    void annulerReservation_AvecReservationTerminee_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.annulerReservation(1L))
                .thenThrow(new RuntimeException("Impossible d'annuler une réservation terminée"));

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1/annuler"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Impossible d'annuler une réservation terminée"));

        verify(reservationService, times(1)).annulerReservation(1L);
    }

    @Test
    void annulerReservation_AvecReservationInexistante_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.annulerReservation(anyLong()))
                .thenThrow(new RuntimeException("Réservation non trouvée"));

        // Act & Assert
        mockMvc.perform(put("/api/reservations/999/annuler"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Réservation non trouvée"));

        verify(reservationService, times(1)).annulerReservation(999L);
    }

    // ==================== TESTS CONFIRMER RESERVATION ====================

    @Test
    void confirmerReservation_AvecReservationEnAttente_DevraitRetourner200() throws Exception {
        // Arrange
        reservationResponseDTO.setStatut("CONFIRMEE");
        when(reservationService.confirmerReservation(1L)).thenReturn(reservationResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1/confirmer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));

        verify(reservationService, times(1)).confirmerReservation(1L);
    }

    @Test
    void confirmerReservation_AvecReservationDejaConfirmee_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.confirmerReservation(1L))
                .thenThrow(new RuntimeException("Seules les réservations en attente peuvent être confirmées"));

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1/confirmer"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Seules les réservations en attente peuvent être confirmées"));

        verify(reservationService, times(1)).confirmerReservation(1L);
    }

    // ==================== TESTS OBTENIR PAR CODE CONFIRMATION ====================

    @Test
    void obtenirParCodeConfirmation_AvecCodeValide_DevraitRetourner200() throws Exception {
        // Arrange
        when(reservationService.obtenirParCodeConfirmation("RES-12345678"))
                .thenReturn(reservationResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/confirmation/RES-12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codeConfirmation").value("RES-12345678"));

        verify(reservationService, times(1)).obtenirParCodeConfirmation("RES-12345678");
    }

    @Test
    void obtenirParCodeConfirmation_AvecCodeInvalide_DevraitRetourner500() throws Exception {
        // Arrange
        when(reservationService.obtenirParCodeConfirmation(anyString()))
                .thenThrow(new RuntimeException("Réservation non trouvée"));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/confirmation/CODE-INVALIDE"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Réservation non trouvée"));

        verify(reservationService, times(1)).obtenirParCodeConfirmation("CODE-INVALIDE");
    }
}