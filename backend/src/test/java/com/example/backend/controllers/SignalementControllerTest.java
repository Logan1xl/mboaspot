// ===== SignalementControllerTest.java =====
package com.example.backend.controllers;

import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.roles.RoleUtilisateur;
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
class SignalementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SignalementRepository signalementRepository;

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private AdminRepository adminRepository;

    private Annonces annonce;
    private Admin admin;
    private Signalement signalement;

    @BeforeEach
    void setUp() {
        // Nettoyage dans le bon ordre
        signalementRepository.deleteAll();
        adminRepository.deleteAll();
        annoncesRepository.deleteAll();
        proprietaireRepository.deleteAll();
        utilisateurRepository.deleteAll();

        // Créer propriétaire et annonce
        Utilisateur userProprio = TestDataBuilder.createUtilisateur("Proprio", "proprio@test.com");
        userProprio = utilisateurRepository.save(userProprio);

        Proprietaire proprietaire = TestDataBuilder.createProprietaire(userProprio);
        proprietaire = proprietaireRepository.save(proprietaire);

        annonce = TestDataBuilder.createAnnonce(proprietaire);
        annonce = annoncesRepository.save(annonce);

        // Créer admin
        Utilisateur userAdmin = TestDataBuilder.createUtilisateur("Admin", "admin@test.com");
        userAdmin.setRole(RoleUtilisateur.valueOf("ADMIN"));
        userAdmin = utilisateurRepository.save(userAdmin);

        admin = TestDataBuilder.createAdmin(userAdmin);
        admin = adminRepository.save(admin);

        // Créer signalement
        signalement = TestDataBuilder.createSignalement(annonce, admin);
        signalement = signalementRepository.save(signalement);
    }

    @Test
    void testCreer() throws Exception {
        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(admin.getId());
        dto.setRaison("Spam");
        dto.setDescription("Annonce répétée plusieurs fois");

        mockMvc.perform(post("/signalements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.raison").value("Spam"))
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/signalements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].raison").exists());
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(get("/signalements/" + signalement.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(signalement.getId()))
                .andExpect(jsonPath("$.raison").value("Contenu inapproprié"))
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"));
    }

    @Test
    void testTraiter() throws Exception {
        mockMvc.perform(put("/signalements/" + signalement.getId())
                        .param("statut", "TRAITE")
                        .param("resolution", "Annonce retirée"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("TRAITE"))
                .andExpect(jsonPath("$.resolution").value("Annonce retirée"));
    }

    @Test
    void testSupprimer() throws Exception {
        mockMvc.perform(delete("/signalements/" + signalement.getId()))
                .andExpect(status().isNoContent());

        // Vérifier que le signalement n'existe plus
        mockMvc.perform(get("/signalements/" + signalement.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testWorkflowComplet() throws Exception {
        // 1. Créer un nouveau signalement
        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(admin.getId());
        dto.setRaison("Test workflow");
        dto.setDescription("Description du test");

        String response = mockMvc.perform(post("/signalements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long newId = objectMapper.readTree(response).get("id").asLong();

        // 2. Récupérer le signalement
        mockMvc.perform(get("/signalements/" + newId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raison").value("Test workflow"));

        // 3. Traiter le signalement
        mockMvc.perform(put("/signalements/" + newId)
                        .param("statut", "TRAITE")
                        .param("resolution", "Résolu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("TRAITE"));

        // 4. Supprimer le signalement
        mockMvc.perform(delete("/signalements/" + newId))
                .andExpect(status().isNoContent());
    }



    @Test
    void testTraiter_SignalementRejete() throws Exception {
        mockMvc.perform(put("/signalements/" + signalement.getId())
                        .param("statut", "REJETE")
                        .param("resolution", "Signalement non fondé"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("REJETE"))
                .andExpect(jsonPath("$.resolution").value("Signalement non fondé"));
    }
}