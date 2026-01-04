package com.example.backend.services;
import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.entities.*;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.repositories.*;
import com.example.backend.services.implementations.SignalementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignalementServiceTest {

    @Mock
    private SignalementRepository signalementRepository;

    @Mock
    private AnnoncesRepository annoncesRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private SignalementService signalementService;

    private Signalement signalement;
    private SignalementRequestDTO requestDTO;
    private Annonces annonce;
    private Admin admin;

    @BeforeEach
    void setUp() {
        annonce = new Annonces();
        annonce.setId(1L);
        annonce.setTitre("Annonce Test");

        Utilisateur user = new Utilisateur();
        user.setId(1L);

        admin = new Admin();
        admin.setId(1L);
        admin.setIdUser(user);

        signalement = new Signalement();
        signalement.setId(1L);
        signalement.setRaison("Contenu inapproprié");
        signalement.setDescription("Description du signalement");
        signalement.setStatut("EN_ATTENTE");
        signalement.setIdAnnonce(annonce);
        signalement.setIdAdmin(admin);

        requestDTO = new SignalementRequestDTO();
        requestDTO.setAnnonceId(1L);
        requestDTO.setAdminId(1L);
        requestDTO.setRaison("Contenu inapproprié");
        requestDTO.setDescription("Description du signalement");
    }

    @Test
    void creer_Success() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(signalementRepository.save(any(Signalement.class))).thenReturn(signalement);

        // When
        Signalement result = signalementService.creer(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Contenu inapproprié", result.getRaison());
        assertEquals("EN_ATTENTE", result.getStatut());

        verify(annoncesRepository).findById(1L);
        verify(adminRepository).findById(1L);
        verify(signalementRepository).save(any(Signalement.class));
    }

    @Test
    void creer_AnnonceNotFound() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            signalementService.creer(requestDTO);
        });

        verify(signalementRepository, never()).save(any());
    }

    @Test
    void getAll_Success() {
        // Given
        List<Signalement> signalements = Arrays.asList(signalement);
        when(signalementRepository.findAll()).thenReturn(signalements);

        // When
        List<Signalement> result = signalementService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(signalementRepository).findAll();
    }

    @Test
    void getById_Success() {
        // Given
        when(signalementRepository.findById(1L)).thenReturn(Optional.of(signalement));

        // When
        Signalement result = signalementService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(signalementRepository).findById(1L);
    }

    @Test
    void getById_NotFound() {
        // Given
        when(signalementRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            signalementService.getById(1L);
        });
    }

    @Test
    void traiter_Success() {
        // Given
        when(signalementRepository.findById(1L)).thenReturn(Optional.of(signalement));
        when(signalementRepository.save(any(Signalement.class))).thenReturn(signalement);

        // When
        Signalement result = signalementService.traiter(1L, "TRAITE", "Résolution appliquée");

        // Then
        assertNotNull(result);
        verify(signalementRepository).save(any(Signalement.class));
    }

    @Test
    void supprimer_Success() {
        // Given
        doNothing().when(signalementRepository).deleteById(1L);

        // When
        signalementService.supprimer(1L);

        // Then
        verify(signalementRepository).deleteById(1L);
    }
}