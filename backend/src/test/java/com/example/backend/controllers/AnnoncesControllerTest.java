package com.example.backend.controllers;

import com.example.backend.dto.AnnonceDTO;
import com.example.backend.dto.RechercheDTO;
import com.example.backend.services.AnnoncesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AnnoncesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnnoncesService annoncesService;

    private AnnonceDTO annonceTest;

    @BeforeEach
    void setUp() {
        // Créer une annonce de test
        annonceTest = new AnnonceDTO();
        annonceTest.setTitre("Appartement moderne");
        annonceTest.setPrix(50000.0);
        annonceTest.setAdresse("123 Rue Test, Douala");
        annonceTest.setVille("Douala");
        annonceTest.setLatitude(4.0511);
        annonceTest.setLongitude(9.7679);
        annonceTest.setNbreChambres(3);
        annonceTest.setNbreLits(2);
        annonceTest.setMaxInvites(6);
        annonceTest.setDescription("Bel appartement au centre-ville");
        annonceTest.setTypeAnnonce("APPARTEMENT");
        annonceTest.setUrlImagePrincipale("http://example.com/image.jpg");
        annonceTest.setUrlImages(Arrays.asList("img1.jpg", "img2.jpg"));
    }

    @Test
    void testGetAllAnnonces() throws Exception {
        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void testGetAnnoncesActives() throws Exception {
        mockMvc.perform(get("/api/annonces/actives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void testCreateAnnonce() throws Exception {
        String annonceJson = objectMapper.writeValueAsString(annonceTest);

        mockMvc.perform(post("/api/annonces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(annonceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titre", is("Appartement moderne")))
                .andExpect(jsonPath("$.prix", is(50000.0)))
                .andExpect(jsonPath("$.ville", is("Douala")));
    }

    @Test
    void testGetAnnonceById() throws Exception {
        // Créer d'abord une annonce
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        mockMvc.perform(get("/api/annonces/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())))
                .andExpect(jsonPath("$.titre", is("Appartement moderne")));
    }

    @Test
    void testGetAnnonceById_NotFound() throws Exception {
        mockMvc.perform(get("/api/annonces/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAnnonce() throws Exception {
        // Créer une annonce
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        // Modifier l'annonce
        created.setTitre("Appartement modifié");
        created.setPrix(60000.0);
        String updatedJson = objectMapper.writeValueAsString(created);

        mockMvc.perform(put("/api/annonces/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre", is("Appartement modifié")))
                .andExpect(jsonPath("$.prix", is(60000.0)));
    }

    @Test
    void testDeleteAnnonce() throws Exception {
        // Créer une annonce
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        mockMvc.perform(delete("/api/annonces/" + created.getId()))
                .andExpect(status().isNoContent());

        // Vérifier que l'annonce n'existe plus
        mockMvc.perform(get("/api/annonces/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActiverAnnonce() throws Exception {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        mockMvc.perform(patch("/api/annonces/" + created.getId() + "/activer")
                        .param("activer", "false"))
                .andExpect(status().isOk());
    }

    @Test
    void testRechercherAnnonces() throws Exception {
        // Créer quelques annonces
        annoncesService.createAnnonce(annonceTest);

        RechercheDTO recherche = new RechercheDTO();
        recherche.setVille("Douala");
        recherche.setPrixMin(0.0);
        recherche.setPrixMax(100000.0);

        String rechercheJson = objectMapper.writeValueAsString(recherche);

        mockMvc.perform(post("/api/annonces/recherche")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rechercheJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void testGetTopAnnonces() throws Exception {
        mockMvc.perform(get("/api/annonces/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void testGetVillesDisponibles() throws Exception {
        mockMvc.perform(get("/api/annonces/villes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void testGetQuartiers() throws Exception {
        mockMvc.perform(get("/api/annonces/quartiers")
                        .param("ville", "Douala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }
}