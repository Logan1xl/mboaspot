package com.example.backend.reservationservice;


import com.example.backend.entities.Annonces;
import com.example.backend.entities.Disponibilite;
import com.example.backend.entities.Proprietaire;
import com.example.backend.entityDTO.reservationdto.AnnonceRequestDTO;
import com.example.backend.entityDTO.reservationdto.AnnonceResponseDTO;
import com.example.backend.repository.AnnoncesRepository;
import com.example.backend.repository.DisponibiliteRepository;
import com.example.backend.repository.ProprietaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnonceServiceTest {

    @Mock
    private AnnoncesRepository annoncesRepository;

    @Mock
    private ProprietaireRepository proprietaireRepository;

    @Mock
    private DisponibiliteRepository disponibiliteRepository;

    @InjectMocks
    private AnnonceService annonceService;

    private Annonces annonce;
    private Proprietaire proprietaire;
    private AnnonceRequestDTO annonceRequestDTO;

    @BeforeEach
    void setUp() {
        // Configuration du propriétaire
        proprietaire = new Proprietaire();
        proprietaire.setId(1L);
        proprietaire.setNomEntreprise("Entreprise Test");

        // Configuration de l'annonce
        annonce = new Annonces();
        annonce.setId(1L);
        annonce.setTitre("Belle maison à Douala");
        annonce.setPrix(50000.0);
        annonce.setAdresse("Akwa, Douala");
        annonce.setVille("Douala");
        annonce.setNbreChambres(3);
        annonce.setNbreLits(2);
        annonce.setMaxInvites(4);
        annonce.setDescription("Une magnifique maison au centre ville");
        annonce.setTypeAnnonce("MAISON");
        annonce.setIdProprietaire(proprietaire);
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

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
    }

    // ==================== TESTS CREER ANNONCE ====================

    @Test
    void creerAnnonce_AvecDonneesValides_DevraitCreerAnnonce() {
        // Arrange
        when(proprietaireRepository.findById(anyLong())).thenReturn(Optional.of(proprietaire));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);
        when(disponibiliteRepository.save(any(Disponibilite.class))).thenReturn(new Disponibilite());

        // Act
        AnnonceResponseDTO response = annonceService.creerAnnonce(annonceRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Belle maison à Douala", response.getTitre());
        assertEquals(50000.0, response.getPrix());
        assertEquals("Akwa, Douala", response.getAdresse());
        assertEquals("Douala", response.getVille());
        assertEquals(3, response.getNbreChambres());
        assertEquals(2, response.getNbreLits());
        assertEquals(4, response.getMaxInvites());
        assertEquals("Une magnifique maison au centre ville", response.getDescription());
        assertEquals("MAISON", response.getTypeAnnonce());
        assertTrue(response.getEstActive());
        assertEquals(0.0, response.getEvaluationMoyenne());
        assertEquals(0, response.getTotalAvis());
        assertEquals(1L, response.getProprietaireId());
        assertEquals("Entreprise Test", response.getProprietaireNom());

        verify(proprietaireRepository).findById(1L);
        verify(annoncesRepository).save(any(Annonces.class));
        verify(disponibiliteRepository).save(any(Disponibilite.class));
    }

    @Test
    void creerAnnonce_AvecProprietaireInexistant_DevraitLancerException() {
        // Arrange
        when(proprietaireRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> annonceService.creerAnnonce(annonceRequestDTO));

        assertEquals("Propriétaire non trouvé", exception.getMessage());
        verify(proprietaireRepository).findById(1L);
        verify(annoncesRepository, never()).save(any(Annonces.class));
    }

    @Test
    void creerAnnonce_DevraitCreerDisponibiliteParDefaut() {
        // Arrange
        when(proprietaireRepository.findById(anyLong())).thenReturn(Optional.of(proprietaire));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);
        when(disponibiliteRepository.save(any(Disponibilite.class))).thenReturn(new Disponibilite());

        // Act
        annonceService.creerAnnonce(annonceRequestDTO);

        // Assert
        ArgumentCaptor<Disponibilite> disponibiliteCaptor = ArgumentCaptor.forClass(Disponibilite.class);
        verify(disponibiliteRepository).save(disponibiliteCaptor.capture());

        Disponibilite savedDisponibilite = disponibiliteCaptor.getValue();
        assertTrue(savedDisponibilite.getEstDisponible());
        assertEquals(0.0, savedDisponibilite.getPrixSurcharge());
        assertNotNull(savedDisponibilite.getDateDebut());
        assertNotNull(savedDisponibilite.getDateFin());
    }

    @Test
    void creerAnnonce_DevraitDefinirAnnonceActiveParDefaut() {
        // Arrange
        when(proprietaireRepository.findById(anyLong())).thenReturn(Optional.of(proprietaire));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);
        when(disponibiliteRepository.save(any(Disponibilite.class))).thenReturn(new Disponibilite());

        // Act
        annonceService.creerAnnonce(annonceRequestDTO);

        // Assert
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces savedAnnonce = annonceCaptor.getValue();
        assertTrue(savedAnnonce.getEstActive());
        assertEquals(0.0, savedAnnonce.getEvaluationMoyenne());
        assertEquals(0, savedAnnonce.getTotalAvis());
    }

    @Test
    void creerAnnonce_DevraitConserverToutesLesInformations() {
        // Arrange
        when(proprietaireRepository.findById(anyLong())).thenReturn(Optional.of(proprietaire));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);
        when(disponibiliteRepository.save(any(Disponibilite.class))).thenReturn(new Disponibilite());

        // Act
        annonceService.creerAnnonce(annonceRequestDTO);

        // Assert
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces savedAnnonce = annonceCaptor.getValue();
        assertEquals("Belle maison à Douala", savedAnnonce.getTitre());
        assertEquals(50000.0, savedAnnonce.getPrix());
        assertEquals("Akwa, Douala", savedAnnonce.getAdresse());
        assertEquals("Douala", savedAnnonce.getVille());
        assertEquals(3, savedAnnonce.getNbreChambres());
        assertEquals(2, savedAnnonce.getNbreLits());
        assertEquals(4, savedAnnonce.getMaxInvites());
        assertEquals("Une magnifique maison au centre ville", savedAnnonce.getDescription());
        assertEquals("MAISON", savedAnnonce.getTypeAnnonce());
        assertEquals(proprietaire, savedAnnonce.getIdProprietaire());
    }

    // ==================== TESTS OBTENIR ANNONCE ====================

    @Test
    void obtenirAnnonce_AvecIdValide_DevraitRetournerAnnonce() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));

        // Act
        AnnonceResponseDTO response = annonceService.obtenirAnnonce(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Belle maison à Douala", response.getTitre());
        verify(annoncesRepository).findById(1L);
    }

    @Test
    void obtenirAnnonce_AvecIdInexistant_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> annonceService.obtenirAnnonce(999L));

        assertEquals("Annonce non trouvée", exception.getMessage());
    }

    // ==================== TESTS OBTENIR ANNONCES ACTIVES ====================

    @Test
    void obtenirAnnoncesActives_DevraitRetournerSeulementAnnoncesActives() {
        // Arrange
        List<Annonces> annonces = List.of(annonce);
        when(annoncesRepository.findByEstActive(true)).thenReturn(annonces);

        // Act
        List<AnnonceResponseDTO> response = annonceService.obtenirAnnoncesActives();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertTrue(response.get(0).getEstActive());
        verify(annoncesRepository).findByEstActive(true);
    }

    @Test
    void obtenirAnnoncesActives_SansAnnoncesActives_DevraitRetournerListeVide() {
        // Arrange
        when(annoncesRepository.findByEstActive(true)).thenReturn(Collections.emptyList());

        // Act
        List<AnnonceResponseDTO> response = annonceService.obtenirAnnoncesActives();

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    // ==================== TESTS OBTENIR ANNONCES PROPRIETAIRE ====================

    @Test
    void obtenirAnnoncesProprietaire_AvecProprietaireAyantAnnonces_DevraitRetournerListe() {
        // Arrange
        List<Annonces> annonces = List.of(annonce);
        when(annoncesRepository.findByIdProprietaire_Id(1L)).thenReturn(annonces);

        // Act
        List<AnnonceResponseDTO> response = annonceService.obtenirAnnoncesProprietaire(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getProprietaireId());
        verify(annoncesRepository).findByIdProprietaire_Id(1L);
    }

    @Test
    void obtenirAnnoncesProprietaire_AvecProprietaireSansAnnonces_DevraitRetournerListeVide() {
        // Arrange
        when(annoncesRepository.findByIdProprietaire_Id(1L)).thenReturn(Collections.emptyList());

        // Act
        List<AnnonceResponseDTO> response = annonceService.obtenirAnnoncesProprietaire(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    // ==================== TESTS CHANGER STATUT ANNONCE ====================

    @Test
    void changerStatutAnnonce_DevraitActiverAnnonce() {
        // Arrange
        annonce.setEstActive(false);
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // Act
        AnnonceResponseDTO response = annonceService.changerStatutAnnonce(1L, true);

        // Assert
        assertNotNull(response);
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces savedAnnonce = annonceCaptor.getValue();
        assertTrue(savedAnnonce.getEstActive());
    }

    @Test
    void changerStatutAnnonce_DevraitDesactiverAnnonce() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // Act
        AnnonceResponseDTO response = annonceService.changerStatutAnnonce(1L, false);

        // Assert
        assertNotNull(response);
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces savedAnnonce = annonceCaptor.getValue();
        assertFalse(savedAnnonce.getEstActive());
    }

    @Test
    void changerStatutAnnonce_AvecAnnonceInexistante_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> annonceService.changerStatutAnnonce(999L, true));

        assertEquals("Annonce non trouvée", exception.getMessage());
        verify(annoncesRepository, never()).save(any(Annonces.class));
    }

    // ==================== TESTS METTRE A JOUR ANNONCE ====================

    @Test
    void mettreAJourAnnonce_AvecDonneesValides_DevraitMettreAJour() {
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

        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // Act
        annonceService.mettreAJourAnnonce(1L, updateDTO);

        // Assert
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces updatedAnnonce = annonceCaptor.getValue();
        assertEquals("Nouvelle maison", updatedAnnonce.getTitre());
        assertEquals(60000.0, updatedAnnonce.getPrix());
        assertEquals("Bonanjo, Douala", updatedAnnonce.getAdresse());
        assertEquals("Douala", updatedAnnonce.getVille());
        assertEquals(4, updatedAnnonce.getNbreChambres());
        assertEquals(3, updatedAnnonce.getNbreLits());
        assertEquals(6, updatedAnnonce.getMaxInvites());
        assertEquals("Description mise à jour", updatedAnnonce.getDescription());
        assertEquals("APPARTEMENT", updatedAnnonce.getTypeAnnonce());
    }

    @Test
    void mettreAJourAnnonce_AvecAnnonceInexistante_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> annonceService.mettreAJourAnnonce(999L, annonceRequestDTO));

        assertEquals("Annonce non trouvée", exception.getMessage());
        verify(annoncesRepository, never()).save(any(Annonces.class));
    }

    // ==================== TESTS SUPPRIMER ANNONCE ====================

    @Test
    void supprimerAnnonce_DevraitDesactiverAnnonce() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(annoncesRepository.save(any(Annonces.class))).thenReturn(annonce);

        // Act
        annonceService.supprimerAnnonce(1L);

        // Assert
        ArgumentCaptor<Annonces> annonceCaptor = ArgumentCaptor.forClass(Annonces.class);
        verify(annoncesRepository).save(annonceCaptor.capture());

        Annonces savedAnnonce = annonceCaptor.getValue();
        assertFalse(savedAnnonce.getEstActive());
        verify(annoncesRepository, never()).delete(any(Annonces.class));
    }

    @Test
    void supprimerAnnonce_AvecAnnonceInexistante_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> annonceService.supprimerAnnonce(999L));

        assertEquals("Annonce non trouvée", exception.getMessage());
        verify(annoncesRepository, never()).save(any(Annonces.class));
    }
}