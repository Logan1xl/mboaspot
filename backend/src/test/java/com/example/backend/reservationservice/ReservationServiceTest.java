package com.example.backend.reservationservice;

import com.example.backend.entities.Annonces;
import com.example.backend.entities.Disponibilite;
import com.example.backend.entities.Reservation;
import com.example.backend.entities.Voyageur;
import com.example.backend.dto.reservationdto.DisponibiliteRequestDTO;
import com.example.backend.dto.reservationdto.ReservationRequestDTO;
import com.example.backend.dto.reservationdto.ReservationResponseDTO;
import com.example.backend.repository.AnnoncesRepository;
import com.example.backend.repository.DisponibiliteRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.VoyageurRepository;
import com.example.backend.services.reservationservice.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AnnoncesRepository annoncesRepository;

    @Mock
    private VoyageurRepository voyageurRepository;

    @Mock
    private DisponibiliteRepository disponibiliteRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Annonces annonce;
    private Voyageur voyageur;
    private Reservation reservation;
    private ReservationRequestDTO reservationRequestDTO;
    private DisponibiliteRequestDTO disponibiliteRequestDTO;
    private Disponibilite disponibilite;

    @BeforeEach
    void setUp() {
        // Configuration de l'annonce
        annonce = new Annonces();
        annonce.setId(1L);
        annonce.setTitre("Belle maison à Douala");
        annonce.setPrix(50000.0);
        annonce.setVille("Douala");
        annonce.setMaxInvites(4);
        annonce.setEstActive(true);

        // Configuration du voyageur
        voyageur = new Voyageur();
        voyageur.setId(1L);

        // Configuration de la disponibilité
        disponibilite = new Disponibilite();
        disponibilite.setId(1L);
        disponibilite.setIdAnnonce(annonce);
        disponibilite.setDateDebut(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        disponibilite.setDateFin(cal.getTime());
        disponibilite.setEstDisponible(true);

        // Configuration de la réservation
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setAnnonce(annonce);
        reservation.setIdVoyageur(voyageur);
        reservation.setDateDebut(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setDateFin(Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setNombreInvites(2);
        reservation.setPrixTotal(100000.0);
        reservation.setCodeConfirmation("RES-12345678");
        reservation.setStatut("EN_ATTENTE");

        // Configuration du DTO de requête de réservation
        reservationRequestDTO = new ReservationRequestDTO();
        reservationRequestDTO.setAnnonceId(1L);
        reservationRequestDTO.setVoyageurId(1L);
        reservationRequestDTO.setDateDebut(LocalDate.now().plusDays(1));
        reservationRequestDTO.setDateFin(LocalDate.now().plusDays(3));
        reservationRequestDTO.setNombreInvites(2);

        // Configuration du DTO de disponibilité
        disponibiliteRequestDTO = new DisponibiliteRequestDTO();
        disponibiliteRequestDTO.setAnnonceId(1L);
        disponibiliteRequestDTO.setDateDebut(LocalDate.now().plusDays(1));
        disponibiliteRequestDTO.setDateFin(LocalDate.now().plusDays(3));
        disponibiliteRequestDTO.setNombreInvites(2);
    }

    // ==================== TESTS VERIFIER DISPONIBILITE ====================

    @Test
    void verifierDisponibilite_AvecDonneesValides_DevraitRetournerTrue() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertTrue(resultat);
        verify(annoncesRepository).findById(1L);
        verify(disponibiliteRepository).findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class));
        verify(disponibiliteRepository).findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class));
        verify(reservationRepository).findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class));
    }

    @Test
    void verifierDisponibilite_AvecAnnonceInexistante_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.verifierDisponibilite(disponibiliteRequestDTO));

        assertEquals("Annonce non trouvée", exception.getMessage());
        verify(annoncesRepository).findById(1L);
    }

    @Test
    void verifierDisponibilite_AvecAnnonceInactive_DevraitRetournerFalse() {
        // Arrange
        annonce.setEstActive(false);
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertFalse(resultat);
        verify(annoncesRepository).findById(1L);
    }

    @Test
    void verifierDisponibilite_AvecDateDebutApresDateFin_DevraitLancerException() {
        // Arrange
        disponibiliteRequestDTO.setDateDebut(LocalDate.now().plusDays(5));
        disponibiliteRequestDTO.setDateFin(LocalDate.now().plusDays(3));

        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.verifierDisponibilite(disponibiliteRequestDTO));

        assertEquals("Date de début après la date de fin", exception.getMessage());
    }

    @Test
    void verifierDisponibilite_AvecDatePassee_DevraitLancerException() {
        // Arrange
        disponibiliteRequestDTO.setDateDebut(LocalDate.now().minusDays(2));
        disponibiliteRequestDTO.setDateFin(LocalDate.now().plusDays(1));

        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.verifierDisponibilite(disponibiliteRequestDTO));

        assertEquals("date déjà passé", exception.getMessage());
    }

    @Test
    void verifierDisponibilite_AvecTropDInvites_DevraitRetournerFalse() {
        // Arrange
        disponibiliteRequestDTO.setNombreInvites(10);
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertFalse(resultat);
        verify(annoncesRepository).findById(1L);
    }

    @Test
    void verifierDisponibilite_SansDisponibiliteCouvrante_DevraitRetournerFalse() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertFalse(resultat);
    }

    @Test
    void verifierDisponibilite_AvecPeriodeBloquee_DevraitRetournerFalse() {
        // Arrange
        Disponibilite periodeBloquee = new Disponibilite();
        periodeBloquee.setEstDisponible(false);

        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(periodeBloquee));

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertFalse(resultat);
    }

    @Test
    void verifierDisponibilite_AvecReservationChevauchante_DevraitRetournerFalse() {
        // Arrange
        when(annoncesRepository.findById(anyLong())).thenReturn(Optional.of(annonce));
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(reservation));

        // Act
        boolean resultat = reservationService.verifierDisponibilite(disponibiliteRequestDTO);

        // Assert
        assertFalse(resultat);
    }

    // ==================== TESTS CREER RESERVATION ====================

    @Test
    void creerReservation_AvecDonneesValides_DevraitCreerReservation() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(voyageurRepository.findById(1L)).thenReturn(Optional.of(voyageur));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Mocks pour la vérification de disponibilité
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        ReservationResponseDTO response = reservationService.creerReservation(reservationRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("RES-12345678", response.getCodeConfirmation());
        assertEquals(2, response.getNombreInvites());
        assertEquals(100000.0, response.getPrixTotal());
        assertEquals("EN_ATTENTE", response.getStatut());
        assertEquals(1L, response.getAnnonceId());
        assertEquals("Belle maison à Douala", response.getAnnonceTitre());
        assertEquals("Douala", response.getAnnonceVille());
        assertEquals(1L, response.getVoyageurId());

        // findById est appelé 2 fois : 1 fois dans verifierDisponibilite() et 1 fois dans creerReservation()
        verify(annoncesRepository, times(2)).findById(1L);
        verify(voyageurRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void creerReservation_AvecAnnonceNonDisponible_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.creerReservation(reservationRequestDTO));

        assertEquals("anonces non disponible", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void creerReservation_AvecAnnonceInexistante_DevraitLancerException() {
        // Arrange
        // Premier appel dans verifierDisponibilite() : annonce existe
        // Deuxième appel dans creerReservation() : annonce n'existe pas
        when(annoncesRepository.findById(1L))
                .thenReturn(Optional.of(annonce))  // 1er appel dans verifierDisponibilite()
                .thenReturn(Optional.empty());      // 2ème appel dans creerReservation()

        // Mocks pour que verifierDisponibilite() retourne true
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.creerReservation(reservationRequestDTO));

        assertEquals("Annonce non trouvée", exception.getMessage());
    }

    @Test
    void creerReservation_AvecVoyageurInexistant_DevraitLancerException() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(voyageurRepository.findById(1L)).thenReturn(Optional.empty());

        // Mocks pour la vérification de disponibilité
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.creerReservation(reservationRequestDTO));

        assertEquals("Voyageur non disponible", exception.getMessage());
    }

    @Test
    void creerReservation_DevraitCalculerPrixCorrectement() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(voyageurRepository.findById(1L)).thenReturn(Optional.of(voyageur));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Mocks pour la vérification de disponibilité
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        reservationService.creerReservation(reservationRequestDTO);

        // Assert
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());

        Reservation savedReservation = reservationCaptor.getValue();
        // 2 nuits * 50000 = 100000
        assertEquals(100000.0, savedReservation.getPrixTotal());
    }

    @Test
    void creerReservation_DevraitGenererCodeConfirmation() {
        // Arrange
        when(annoncesRepository.findById(1L)).thenReturn(Optional.of(annonce));
        when(voyageurRepository.findById(1L)).thenReturn(Optional.of(voyageur));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Mocks pour la vérification de disponibilité
        when(disponibiliteRepository.findDisponibiliteCouvrante(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(List.of(disponibilite));
        when(disponibiliteRepository.findPeriodesBloquees(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.findReservationsChevauchantes(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // Act
        reservationService.creerReservation(reservationRequestDTO);

        // Assert
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());

        Reservation savedReservation = reservationCaptor.getValue();
        assertNotNull(savedReservation.getCodeConfirmation());
        assertTrue(savedReservation.getCodeConfirmation().startsWith("RES-"));
    }

    // ==================== TESTS OBTENIR RESERVATION ====================

    @Test
    void obtenirReservation_AvecIdValide_DevraitRetournerReservation() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        ReservationResponseDTO response = reservationService.obtenirReservation(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("RES-12345678", response.getCodeConfirmation());
        verify(reservationRepository).findById(1L);
    }

    @Test
    void obtenirReservation_AvecIdInexistant_DevraitLancerException() {
        // Arrange
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.obtenirReservation(999L));

        assertEquals("Réservation non trouvée", exception.getMessage());
    }

    // ==================== TESTS ANNULER RESERVATION ====================

    @Test
    void annulerReservation_AvecReservationValide_DevraitAnnuler() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationResponseDTO response = reservationService.annulerReservation(1L);

        // Assert
        assertNotNull(response);
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());

        Reservation savedReservation = reservationCaptor.getValue();
        assertEquals("ANNULEE", savedReservation.getStatut());
    }

    @Test
    void annulerReservation_AvecReservationTerminee_DevraitLancerException() {
        // Arrange
        reservation.setStatut("TERMINEE");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.annulerReservation(1L));

        assertEquals("Impossible d'annuler une réservation terminée", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    // ==================== TESTS CONFIRMER RESERVATION ====================

    @Test
    void confirmerReservation_AvecReservationEnAttente_DevraitConfirmer() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationResponseDTO response = reservationService.confirmerReservation(1L);

        // Assert
        assertNotNull(response);
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(reservationCaptor.capture());

        Reservation savedReservation = reservationCaptor.getValue();
        assertEquals("CONFIRMEE", savedReservation.getStatut());
    }

    @Test
    void confirmerReservation_AvecReservationDejaConfirmee_DevraitLancerException() {
        // Arrange
        reservation.setStatut("CONFIRMEE");
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.confirmerReservation(1L));

        assertEquals("Seules les réservations en attente peuvent être confirmées", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    // ==================== TESTS OBTENIR PAR CODE CONFIRMATION ====================

    @Test
    void obtenirParCodeConfirmation_AvecCodeValide_DevraitRetournerReservation() {
        // Arrange
        when(reservationRepository.findByCodeConfirmation("RES-12345678"))
                .thenReturn(Optional.of(reservation));

        // Act
        ReservationResponseDTO response = reservationService.obtenirParCodeConfirmation("RES-12345678");

        // Assert
        assertNotNull(response);
        assertEquals("RES-12345678", response.getCodeConfirmation());
        verify(reservationRepository).findByCodeConfirmation("RES-12345678");
    }

    @Test
    void obtenirParCodeConfirmation_AvecCodeInvalide_DevraitLancerException() {
        // Arrange
        when(reservationRepository.findByCodeConfirmation(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.obtenirParCodeConfirmation("CODE-INVALIDE"));

        assertEquals("Réservation non trouvée", exception.getMessage());
    }

    // ==================== TESTS OBTENIR RESERVATIONS VOYAGEUR ====================

    @Test
    void obtenirReservationsVoyageur_AvecVoyageurAyantReservations_DevraitRetournerListe() {
        // Arrange
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findByIdVoyageur_IdOrderByDateDebutDesc(1L))
                .thenReturn(reservations);

        // Act
        List<ReservationResponseDTO> response = reservationService.obtenirReservationsVoyageur(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        verify(reservationRepository).findByIdVoyageur_IdOrderByDateDebutDesc(1L);
    }

    @Test
    void obtenirReservationsVoyageur_AvecVoyageurSansReservations_DevraitRetournerListeVide() {
        // Arrange
        when(reservationRepository.findByIdVoyageur_IdOrderByDateDebutDesc(1L))
                .thenReturn(Collections.emptyList());

        // Act
        List<ReservationResponseDTO> response = reservationService.obtenirReservationsVoyageur(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}