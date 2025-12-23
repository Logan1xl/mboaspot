// ===== AnnoncesControllerTest.java =====
package com.example.backend.controllers;

import com.example.backend.dto.*;
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

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
class AnnoncesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private LocalisationRepository localisationRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    private Proprietaire proprietaire;
    private Annonces annonce;

    @BeforeEach
    void setUp() {
        disponibiliteRepository.deleteAll();
        localisationRepository.deleteAll();
        annoncesRepository.deleteAll();
        proprietaireRepository.deleteAll();
        utilisateurRepository.deleteAll();

        Utilisateur user = TestDataBuilder.createUtilisateur("Dupont", "dupont@test.com");
        user = utilisateurRepository.save(user);

        proprietaire = TestDataBuilder.createProprietaire(user);
        proprietaire = proprietaireRepository.save(proprietaire);

        annonce = TestDataBuilder.createAnnonce(proprietaire);
        annonce = annoncesRepository.save(annonce);
    }

    @Test
    void testGetAllAnnonces() throws Exception {
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].titre").value("Test Annonce"));
    }

    @Test
    void testGetAnnoncesActives() throws Exception {
        Annonces annonceInactive = TestDataBuilder.createAnnonce(proprietaire);
        annonceInactive.setEstActive(false);
        annoncesRepository.save(annonceInactive);

        mockMvc.perform(get("/api/annonces/actives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estActive").value(true));
    }

    @Test
    void testGetAnnonceById() throws Exception {
        mockMvc.perform(get("/api/annonces/" + annonce.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(annonce.getId()))
                .andExpect(jsonPath("$.titre").value("Test Annonce"))
                .andExpect(jsonPath("$.prix").value(100.0));
    }

    @Test
    void testGetAnnonceById_NotFound() throws Exception {
        mockMvc.perform(get("/api/annonces/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAnnonce() throws Exception {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitre("Nouvelle Annonce");
        dto.setPrix(200.0);
        dto.setAdresse("456 New Street");
        dto.setVille("Yaoundé");
        dto.setLatitude(3.87);
        dto.setLongitude(11.52);
        dto.setNbreChambres(3);
        dto.setNbreLits(3);
        dto.setMaxInvites(6);
        dto.setDescription("Description test");
        dto.setTypeAnnonce("Maison");
        dto.setUrlImagePrincipale("https://test.com/image.jpg");
        dto.setUrlImages(Arrays.asList("img1.jpg", "img2.jpg"));
        dto.setIdProprietaire(proprietaire.getId());

        mockMvc.perform(post("/api/annonces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre").value("Nouvelle Annonce"))
                .andExpect(jsonPath("$.prix").value(200.0));
    }

    @Test
    void testUpdateAnnonce() throws Exception {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitre("Titre Modifié");
        dto.setPrix(250.0);
        dto.setAdresse(annonce.getAdresse());
        dto.setVille(annonce.getVille());
        dto.setNbreChambres(annonce.getNbreChambres());
        dto.setNbreLits(annonce.getNbreLits());
        dto.setMaxInvites(annonce.getMaxInvites());
        dto.setDescription("Description modifiée");
        dto.setTypeAnnonce(annonce.getTypeAnnonce());

        mockMvc.perform(put("/api/annonces/" + annonce.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Titre Modifié"))
                .andExpect(jsonPath("$.prix").value(250.0));
    }

    @Test
    void testDeleteAnnonce() throws Exception {
        mockMvc.perform(delete("/api/annonces/" + annonce.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/annonces/" + annonce.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActiverAnnonce() throws Exception {
        mockMvc.perform(patch("/api/annonces/" + annonce.getId() + "/activer")
                        .param("activer", "false"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/annonces/" + annonce.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estActive").value(false));
    }

    @Test
    void testAddDisponibilite() throws Exception {
        DisponibiliteDTO dto = new DisponibiliteDTO();
        dto.setIdAnnonce(annonce.getId());
        dto.setEstDisponible(true);
        dto.setDateDebut(new Date());
        dto.setDateFin(new Date(System.currentTimeMillis() + 86400000L * 7));
        dto.setPrixSurcharge(25.0);

        mockMvc.perform(post("/api/annonces/disponibilites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estDisponible").value(true))
                .andExpect(jsonPath("$.prixSurcharge").value(25.0));
    }

    @Test
    void testGetDisponibilites() throws Exception {
        Disponibilite dispo = TestDataBuilder.createDisponibilite(annonce);
        disponibiliteRepository.save(dispo);

        mockMvc.perform(get("/api/annonces/" + annonce.getId() + "/disponibilites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testUpdateLocalisation() throws Exception {
        LocalisationDTO dto = new LocalisationDTO();
        dto.setVille("Yaoundé");
        dto.setQuartier("Bastos");
        dto.setLatitude(3.87);
        dto.setLongitude(11.52);

        mockMvc.perform(put("/api/annonces/" + annonce.getId() + "/localisation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ville").value("Yaoundé"))
                .andExpect(jsonPath("$.quartier").value("Bastos"));
    }

    @Test
    void testGetLocalisation() throws Exception {
        Localisation loc = TestDataBuilder.createLocalisation(annonce);
        localisationRepository.save(loc);

        mockMvc.perform(get("/api/annonces/" + annonce.getId() + "/localisation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ville").value("Douala"))
                .andExpect(jsonPath("$.quartier").value("Akwa"));
    }
}