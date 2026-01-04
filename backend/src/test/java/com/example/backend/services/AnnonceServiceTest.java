package com.example.backend.services;

import com.example.backend.dto.*;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.services.implementations.AnnonceService;
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
class AnnonceServiceTest {

    @Mock
    private AnnoncesRepository annoncesRepository;

    @Mock
    private DisponibiliteRepository disponibiliteRepository;

    @Mock
    private LocalisationRepository localisationRepository;

    @Mock
    private ProprietaireRepository proprietaireRepository;

    @InjectMocks
    private AnnonceService annonceService;

    private Proprietaire proprietaire;
    private Annonces annonce;
    private AnnonceRequestDTO annonceRequestDTO;

    @BeforeEach
    void setUp() {
        // Initialisation du propriétaire
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john@example.com");

        proprietaire = new Proprietaire();
        proprietaire.setId(1L);
        proprietaire.setIdUser(user);
        proprietaire.setNomEntreprise("Entreprise Test");

        // Initialisation de l'annonce
        annonce = new Annonces();
        annonce.setId(1L);
        annonce.setTitre("Belle villa");
        annonce.setPrix(150.0);
        annonce.setAdresse("123 rue Test");
        annonce.setVille("Paris");
        annonce.setNbreChambres(3);
        annonce.setNbreLits(4);
        annonce.setMaxInvites(6);
        annonce.setDescription("Une belle villa");
        annonce.setTypeAnnonce("MAISON");
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);
        annonce.setIdProprietaire(proprietaire);

        // Initialisation du DTO
        annonceRequestDTO = new AnnonceRequestDTO();
        annonceRequestDTO.setTitre("Belle villa");
        annonceRequestDTO.setPrix(150.0);
        annonceRequestDTO.setAdresse("123 rue Test");
        annonceRequestDTO.setVille("Paris");
        annonceRequestDTO.setNbreChambres(3);
        annonceRequestDTO.setNbreLits(4);
        annonceRequestDTO.setMaxInvites(6);
        annonceRequestDTO.setDescription("Une belle villa");
        annonceRequestDTO.setTypeAnnonce("MAISON");
        annonceRequestDTO.setProprietaireId(1L);
    }

    @Test
    void creerAnnonce_Success() {
        // Given
        when(proprietaireRepository.findById(1L)).thenReturn(Optional.of(proprietaire));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);
        when(disponibiliteRepository.save(any(Disponibilite.class))).thenReturn(new Disponibilite());

        // When
        AnnonceResponseDTO result = annonceService.creerAnnonce(annonceRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Belle villa", result.getTitre());
        assertEquals(150.0, result.getPrix());
        assertEquals("Paris", result.getVille());

        verify(proprietaireRepository).findById(1L);
        verify(annoncesRepository).save(any(Annonces.class));
        verify(disponibiliteRepository).save(any(Disponibilite.class));
    }

    @Test
    void creerAnnonce_ProprietaireNotFound() {
        // Given
        when(proprietaireRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            annonceService.creerAnnonce(annonceRequestDTO);
        });

        verify(proprietaireRepository).findById(1L);
        verify(annoncesRepository, never()).save(any(Annonces.class));
    }

    @Test
    void obtenirAnnonce_Success() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));

        // When
        AnnonceResponseDTO result = annonceService.obtenirAnnonce(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Belle villa", result.getTitre());

        verify(annoncesRepository).findById(1L);
    }

    @Test
    void obtenirAnnonce_NotFound() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            annonceService.obtenirAnnonce(1L);
        });

        verify(annoncesRepository).findById(1L);
    }

    @Test
    void obtenirAnnoncesActives_Success() {
        // Given
        List<Annonces> annoncesList = Arrays.asList(annonce);
        when(annoncesRepository.findByEstActive(true)).thenReturn(annoncesList);

        // When
        List<AnnonceResponseDTO> result = annonceService.obtenirAnnoncesActives();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Belle villa", result.get(0).getTitre());

        verify(annoncesRepository).findByEstActive(true);
    }

    @Test
    void mettreAJourAnnonce_Success() {
        // Given
        annonceRequestDTO.setTitre("Villa Mise à Jour");
        annonceRequestDTO.setPrix(200.0);

        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // When
        AnnonceResponseDTO result = annonceService.mettreAJourAnnonce(1L, annonceRequestDTO);

        // Then
        assertNotNull(result);
        verify(annoncesRepository).findById(1L);
        verify(annoncesRepository).save(any(Annonces.class));
    }

    @Test
    void changerStatutAnnonce_Success() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // When
        AnnonceResponseDTO result = annonceService.changerStatutAnnonce(1L, false);

        // Then
        assertNotNull(result);
        verify(annoncesRepository).findById(1L);
        verify(annoncesRepository).save(any(Annonces.class));
    }

    @Test
    void supprimerAnnonce_Success() {
        // Given
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // When
        annonceService.supprimerAnnonce(1L);

        // Then
        verify(annoncesRepository).findById(1L);
        verify(annoncesRepository).save(any(Annonces.class));
    }

    @Test
    void getAllAnnonces_Success() {
        // Given
        List<Annonces> annoncesList = Arrays.asList(annonce);
        when(annoncesRepository.findAll()).thenReturn(annoncesList);

        // When
        List<AnnonceDTO> result = annonceService.getAllAnnonces();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(annoncesRepository).findAll();
    }

    @Test
    void verifierDisponibilite_Available() {
        // Given
        Date dateDebut = new Date();
        Date dateFin = new Date(dateDebut.getTime() + 86400000);
        List<Disponibilite> dispos = Arrays.asList(new Disponibilite());

        when(disponibiliteRepository.findDisponibilitePourPeriode(1L, dateDebut, dateFin))
                .thenReturn(dispos);

        // When
        boolean result = annonceService.verifierDisponibilite(1L, dateDebut, dateFin);

        // Then
        assertTrue(result);
        verify(disponibiliteRepository).findDisponibilitePourPeriode(1L, dateDebut, dateFin);
    }

    @Test
    void verifierDisponibilite_NotAvailable() {
        // Given
        Date dateDebut = new Date();
        Date dateFin = new Date(dateDebut.getTime() + 86400000);

        when(disponibiliteRepository.findDisponibilitePourPeriode(1L, dateDebut, dateFin))
                .thenReturn(Collections.emptyList());

        // When
        boolean result = annonceService.verifierDisponibilite(1L, dateDebut, dateFin);

        // Then
        assertFalse(result);
        verify(disponibiliteRepository).findDisponibilitePourPeriode(1L, dateDebut, dateFin);
    }
}