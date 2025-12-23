
package com.example.backend.controllers;

import com.example.backend.dto.PaiementDTO;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.utils.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
class PaiementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreerPaiement() throws Exception {
        PaiementDTO dto = new PaiementDTO();
        dto.setIdReservation(reservation.getId());
        dto.setMontant(100.0);
        dto.setMethode("CARTE");
        dto.setStatut("EN_ATTENTE");

        mockMvc.perform(post("/api/paiement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.montant").value(100.0))
                .andExpect(jsonPath("$.methode").value("CARTE"));
    }

    @Test
    void testObtenirTousLesPaiements() throws Exception {
        mockMvc.perform(get("/api/paiement"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void testObtenirPaiementParId() throws Exception {
        mockMvc.perform(get("/api/paiement/" + paiement.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paiement.getId()))
                .andExpect(jsonPath("$.montant").value(150.0));
    }

    @Test
    void testMettreAJourStatutPaiement() throws Exception {
        String requestBody = "{\"statut\": \"VALIDE\"}";

        mockMvc.perform(patch("/api/paiement/" + paiement.getId() + "/statut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("VALIDE"));
    }

    @Test
    void testSupprimerPaiement() throws Exception {
        mockMvc.perform(delete("/api/paiement/" + paiement.getId()))
                .andExpect(status().isOk());
    }
}