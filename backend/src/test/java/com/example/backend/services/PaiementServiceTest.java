// ===== PaiementServiceTest.java =====
package com.example.backend.services;
import com.example.backend.dto.PaiementDTO;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.services.implementations.PaiementService;
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
class PaiementServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaiementService paiementService;

    private Paiement paiement;
    private PaiementDTO paiementDTO;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Initialisation de la réservation
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCodeConfirmation("RES-12345");
        reservation.setPrixTotal(300.0);

        // Initialisation du paiement
        paiement = new Paiement();
        paiement.setId(1L);
        paiement.setMontant(300.0);
        paiement.setMethode("CARTE_BANCAIRE");
        paiement.setStatut("VALIDE");
        paiement.setIdTransaction("TXN-ABC123");
        paiement.setIdReservation(reservation);

        // Initialisation du DTO
        paiementDTO = new PaiementDTO();
        paiementDTO.setMontant(300.0);
        paiementDTO.setMethode("CARTE_BANCAIRE");
        paiementDTO.setStatut("EN_ATTENTE");
        paiementDTO.setIdReservation(1L);
    }

    @Test
    void creerPaiement_Success() {
        // Given
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);

        // When
        PaiementDTO result = paiementService.creerPaiement(paiementDTO);

        // Then
        assertNotNull(result);
        assertEquals(300.0, result.getMontant());
        assertNotNull(result.getIdTransaction());

        verify(reservationRepository).findById(1L);
        verify(paiementRepository).save(any(Paiement.class));
    }

    @Test
    void creerPaiement_MontantInvalide() {
        // Given
        paiementDTO.setMontant(-100.0);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            paiementService.creerPaiement(paiementDTO);
        });

        verify(paiementRepository, never()).save(any(Paiement.class));
    }

    @Test
    void creerPaiement_ReservationNotFound() {
        // Given
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            paiementService.creerPaiement(paiementDTO);
        });

        verify(reservationRepository).findById(1L);
        verify(paiementRepository, never()).save(any(Paiement.class));
    }

    @Test
    void obtenirPaiementParId_Success() {
        // Given
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));

        // When
        PaiementDTO result = paiementService.obtenirPaiementParId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(300.0, result.getMontant());

        verify(paiementRepository).findById(1L);
    }

    @Test
    void obtenirPaiementParId_NotFound() {
        // Given
        when(paiementRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            paiementService.obtenirPaiementParId(1L);
        });

        verify(paiementRepository).findById(1L);
    }

    @Test
    void obtenirPaiementsParReservation_Success() {
        // Given
        List<Paiement> paiements = Arrays.asList(paiement);
        when(paiementRepository.findByIdReservation_Id(1L)).thenReturn(paiements);

        // When
        List<PaiementDTO> result = paiementService.obtenirPaiementsParReservation(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(paiementRepository).findByIdReservation_Id(1L);
    }

    @Test
    void mettreAJourStatutPaiement_Success() {
        // Given
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenReturn(paiement);

        // When
        PaiementDTO result = paiementService.mettreAJourStatutPaiement(1L, "VALIDE");

        // Then
        assertNotNull(result);

        verify(paiementRepository).findById(1L);
        verify(paiementRepository).save(any(Paiement.class));
    }

    @Test
    void calculerMontantTotalPaye_Success() {
        // Given
        when(paiementRepository.sumMontantByReservationAndStatut(1L, "VALIDE"))
                .thenReturn(300.0);

        // When
        Double result = paiementService.calculerMontantTotalPaye(1L);

        // Then
        assertEquals(300.0, result);

        verify(paiementRepository).sumMontantByReservationAndStatut(1L, "VALIDE");
    }

    @Test
    void estEntierementPaye_True() {
        // Given
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(paiementRepository.sumMontantByReservationAndStatut(1L, "VALIDE"))
                .thenReturn(300.0);

        // When
        boolean result = paiementService.estEntierementPaye(1L);

        // Then
        assertTrue(result);

        verify(reservationRepository).findById(1L);
        verify(paiementRepository).sumMontantByReservationAndStatut(1L, "VALIDE");
    }

    @Test
    void estEntierementPaye_False() {
        // Given
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(paiementRepository.sumMontantByReservationAndStatut(1L, "VALIDE"))
                .thenReturn(150.0);

        // When
        boolean result = paiementService.estEntierementPaye(1L);

        // Then
        assertFalse(result);
    }

    @Test
    void supprimerPaiement_Success() {
        // Given
        when(paiementRepository.existsById(1L)).thenReturn(true);
        doNothing().when(paiementRepository).deleteById(1L);

        // When
        paiementService.supprimerPaiement(1L);

        // Then
        verify(paiementRepository).existsById(1L);
        verify(paiementRepository).deleteById(1L);
    }
}
