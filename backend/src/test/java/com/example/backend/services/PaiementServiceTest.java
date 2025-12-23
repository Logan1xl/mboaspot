// ===== PaiementServiceTest.java =====
package com.example.backend.services;

import com.example.backend.dto.PaiementDTO;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.utils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
class PaiementServiceTest {

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private VoyageurRepository voyageurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private Reservation reservation;
    private Paiement paiement;

    @BeforeEach
    void setUp() {
        paiementRepository.deleteAll();
        reservationRepository.deleteAll();
        annoncesRepository.deleteAll();
        voyageurRepository.deleteAll();
        proprietaireRepository.deleteAll();
        utilisateurRepository.deleteAll();

        Utilisateur userProprio = TestDataBuilder.createUtilisateur("Proprio", "proprio@test.com");
        userProprio = utilisateurRepository.save(userProprio);
        Proprietaire proprietaire = TestDataBuilder.createProprietaire(userProprio);
        proprietaire = proprietaireRepository.save(proprietaire);
        Annonces annonce = TestDataBuilder.createAnnonce(proprietaire);
        annonce = annoncesRepository.save(annonce);

        Utilisateur userVoyageur = TestDataBuilder.createUtilisateur("Voyageur", "voyageur@test.com");
        userVoyageur = utilisateurRepository.save(userVoyageur);
        Voyageur voyageur = TestDataBuilder.createVoyageur(userVoyageur);
        voyageur = voyageurRepository.save(voyageur);

        reservation = TestDataBuilder.createReservation(annonce, voyageur);
        reservation = reservationRepository.save(reservation);

        paiement = TestDataBuilder.createPaiement(reservation, 150.0);
        paiement = paiementRepository.save(paiement);
    }

    @Test
    void testCreerPaiement() {
        PaiementDTO dto = new PaiementDTO();
        dto.setIdReservation(reservation.getId());
        dto.setMontant(100.0);
        dto.setMethode("MOBILE_MONEY");
        dto.setStatut("EN_ATTENTE");

        PaiementDTO created = paiementService.creerPaiement(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getMontant()).isEqualTo(100.0);
        assertThat(created.getMethode()).isEqualTo("MOBILE_MONEY");
        assertThat(created.getIdTransaction()).isNotNull();
        assertThat(created.getIdTransaction()).startsWith("TXN-");
    }

    @Test
    void testCreerPaiement_MontantInvalide() {
        PaiementDTO dto = new PaiementDTO();
        dto.setIdReservation(reservation.getId());
        dto.setMontant(-50.0);
        dto.setMethode("CARTE");

        assertThatThrownBy(() -> paiementService.creerPaiement(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("montant doit être positif");
    }

    @Test
    void testCreerPaiement_ReservationInexistante() {
        PaiementDTO dto = new PaiementDTO();
        dto.setIdReservation(999L);
        dto.setMontant(100.0);
        dto.setMethode("CARTE");

        assertThatThrownBy(() -> paiementService.creerPaiement(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Réservation introuvable");
    }

    @Test
    void testObtenirTousLesPaiements() {
        List<PaiementDTO> paiements = paiementService.obtenirTousLesPaiements();

        assertThat(paiements).isNotEmpty();
        assertThat(paiements).hasSize(1);
    }

    @Test
    void testObtenirPaiementParId() {
        PaiementDTO found = paiementService.obtenirPaiementParId(paiement.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(paiement.getId());
        assertThat(found.getMontant()).isEqualTo(150.0);
    }

    @Test
    void testObtenirPaiementParId_NotFound() {
        assertThatThrownBy(() -> paiementService.obtenirPaiementParId(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Paiement introuvable");
    }

    @Test
    void testObtenirPaiementsParReservation() {
        Paiement paiement2 = TestDataBuilder.createPaiement(reservation, 50.0);
        paiementRepository.save(paiement2);

        List<PaiementDTO> paiements = paiementService.obtenirPaiementsParReservation(reservation.getId());

        assertThat(paiements).hasSize(2);
        assertThat(paiements).extracting(PaiementDTO::getMontant).contains(150.0, 50.0);
    }

    @Test
    void testMettreAJourPaiement() {
        PaiementDTO dto = new PaiementDTO();
        dto.setMontant(175.0);
        dto.setStatut("VALIDE");
        dto.setUrlRecepisse("https://test.com/recepisse.pdf");

        PaiementDTO updated = paiementService.mettreAJourPaiement(paiement.getId(), dto);

        assertThat(updated.getMontant()).isEqualTo(175.0);
        assertThat(updated.getStatut()).isEqualTo("VALIDE");
        assertThat(updated.getUrlRecepisse()).isEqualTo("https://test.com/recepisse.pdf");
    }

    @Test
    void testMettreAJourStatutPaiement() {
        PaiementDTO updated = paiementService.mettreAJourStatutPaiement(paiement.getId(), "REFUSE");

        assertThat(updated.getStatut()).isEqualTo("REFUSE");
    }

    @Test
    void testSupprimerPaiement() {
        paiementService.supprimerPaiement(paiement.getId());

        assertThat(paiementRepository.existsById(paiement.getId())).isFalse();
    }

    @Test
    void testCalculerMontantTotalPaye() {
        Paiement paiement2 = TestDataBuilder.createPaiement(reservation, 50.0);
        paiementRepository.save(paiement2);

        Double montantTotal = paiementService.calculerMontantTotalPaye(reservation.getId());

        assertThat(montantTotal).isEqualTo(200.0);
    }

    @Test
    void testEstEntierementPaye_True() {
        reservation.setPrixTotal(150.0);
        reservationRepository.save(reservation);

        boolean estPaye = paiementService.estEntierementPaye(reservation.getId());

        assertThat(estPaye).isTrue();
    }

    @Test
    void testEstEntierementPaye_False() {
        reservation.setPrixTotal(300.0);
        reservationRepository.save(reservation);

        boolean estPaye = paiementService.estEntierementPaye(reservation.getId());

        assertThat(estPaye).isFalse();
    }
}