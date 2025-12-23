package com.example.backend.controller;

import com.example.backend.controllers.NotificationController;
import com.example.backend.dto.NotificationDTO;
import com.example.backend.services.implementations.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreerNotification() throws Exception {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        dto.setMessage("Hello");

        Mockito.when(notificationService.creerNotification(any())).thenReturn(dto);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Hello"));
    }

    @Test
    void testObtenirToutesLesNotifications() throws Exception {
        NotificationDTO d1 = new NotificationDTO();
        NotificationDTO d2 = new NotificationDTO();

        Mockito.when(notificationService.obtenirToutesLesNotifications())
                .thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testObtenirNotificationParId_Success() throws Exception {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);

        Mockito.when(notificationService.obtenirNotificationParId(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testObtenirNotificationsParUtilisateur() throws Exception {
        NotificationDTO d1 = new NotificationDTO();
        Mockito.when(notificationService.obtenirNotificationsParUtilisateur(5L))
                .thenReturn(Arrays.asList(d1));

        mockMvc.perform(get("/api/notifications/utilisateur/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void testMettreAJourNotification() throws Exception {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        dto.setMessage("Updated");

        Mockito.when(notificationService.mettreAJourNotification(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Updated"));
    }

    @Test
    void testMarquerCommeLue() throws Exception {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        // Force l'état à true
        dto.setEstLue(true);
        dto.setTitre("Notification Test");

        Mockito.when(notificationService.marquerCommeLue(1L)).thenReturn(dto);

        mockMvc.perform(patch("/api/notifications/1/lue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                // On teste le champ "estLue" car Jackson transforme souvent "lue" en "estLue" pour les booleans
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.estLue").value(true));
    }

    @Test
    void testSupprimerNotification() throws Exception {
        // Pour delete, on vérifie souvent juste le statut HTTP 200
        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isOk());

        Mockito.verify(notificationService).supprimerNotification(1L);
    }
}