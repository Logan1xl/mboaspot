package com.example.backend.service;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entities.Notification;
import com.example.backend.entities.Utilisateur;
import com.example.backend.mappers.NotificationMapper;
import com.example.backend.repositories.NotificationRepository;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.services.implementations.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private NotificationDTO dto;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        notification = new Notification();
        notification.setId(1L);
        notification.setTitre("Alerte");
        notification.setMessage("Message");
        notification.setLue(false);

        dto = new NotificationDTO();
        dto.setId(1L);
        dto.setTitre("Alerte");
        dto.setMessage("Message");
        dto.setIdUser(utilisateur); // Correspond à ton code notificationDTO.getIdUser().getId()
    }

    // --- Tests CREATE ---

    @Test
    void creerNotification_Success() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(notificationMapper.toEntity(any())).thenReturn(notification);
        when(notificationRepository.save(any())).thenReturn(notification);
        when(notificationMapper.toDTO(any())).thenReturn(dto);

        NotificationDTO result = notificationService.creerNotification(dto);
        assertNotNull(result);
        verify(notificationRepository).save(any());
    }

    @Test
    void creerNotification_ThrowsException_WhenInvalid() {
        dto.setTitre(null); // Rend le DTO invalide
        assertThrows(IllegalArgumentException.class, () -> notificationService.creerNotification(dto));
    }

    // --- Tests READ ---

    @Test
    void obtenirToutesLesNotifications() {
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTO(any())).thenReturn(dto);

        List<NotificationDTO> results = notificationService.obtenirToutesLesNotifications();
        assertEquals(1, results.size());
    }

    @Test
    void obtenirNotificationsParUtilisateur() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(notificationRepository.findByIdUser(utilisateur)).thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTO(any())).thenReturn(dto);

        List<NotificationDTO> results = notificationService.obtenirNotificationsParUtilisateur(1L);
        assertFalse(results.isEmpty());
    }

    @Test
    void obtenirNotificationsNonLues() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(notificationRepository.findByIdUserIdAndEstLueFalse(1L)).thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTO(any())).thenReturn(dto);

        List<NotificationDTO> results = notificationService.obtenirNotificationsNonLues(1L);
        assertNotNull(results);
    }

    @Test
    void compterNotificationsNonLues() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(notificationRepository.countByIdUserIdAndEstLueFalse(1L)).thenReturn(5L);

        Long count = notificationService.compterNotificationsNonLues(1L);
        assertEquals(5L, count);
    }

    // --- Tests UPDATE ---

    @Test
    void marquerCommeLue_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any())).thenReturn(notification);
        when(notificationMapper.toDTO(any())).thenReturn(dto);

        notificationService.marquerCommeLue(1L);
        assertTrue(notification.isLue());
    }

    @Test
    void marquerToutesCommeLues() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        when(notificationRepository.findByIdUserIdAndEstLueFalse(1L)).thenReturn(Arrays.asList(notification));

        notificationService.marquerToutesCommeLues(1L);
        verify(notificationRepository).saveAll(anyList());
        assertTrue(notification.isLue());
    }

    // --- Tests DELETE ---

    @Test
    void supprimerNotification_Success() {
        when(notificationRepository.existsById(1L)).thenReturn(true);
        notificationService.supprimerNotification(1L);
        verify(notificationRepository).deleteById(1L);
    }

    @Test
    void supprimerToutesLesNotificationsUtilisateur() {
        when(utilisateurRepository.existsById(1L)).thenReturn(true);
        notificationService.supprimerToutesLesNotificationsUtilisateur(1L);
        verify(notificationRepository).deleteByIdUserId(1L);
    }
}