// ===== AnnoncesServiceTest.java =====
package com.example.backend.services;

import com.example.backend.dto.*;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
class AnnoncesServiceTest {

    @Autowired
    private AnnoncesService annoncesService;

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
    void testGetAllAnnonces() {
        List<AnnonceDTO> annonces = annoncesService.getAllAnnonces();

        assertThat(annonces).isNotEmpty();
        assertThat(annonces).hasSize(1);
        assertThat(annonces.get(0).getTitre()).isEqualTo("Test Annonce");
    }

    @Test
    void testGetAnnoncesActives() {
        Annonces annonceInactive = TestDataBuilder.createAnnonce(proprietaire);
        annonceInactive.setEstActive(false);
        annonceInactive.setTitre("Annonce Inactive");
        annoncesRepository.save(annonceInactive);

        List<AnnonceDTO> annoncesActives = annoncesService.getAnnoncesActives();

        assertThat(annoncesActives).hasSize(1);
        assertThat(annoncesActives.get(0).getEstActive()).isTrue();
    }

    @Test
    void testGetAnnonceById() {
        Optional<AnnonceDTO> result = annoncesService.getAnnonceById(annonce.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitre()).isEqualTo("Test Annonce");
        assertThat(result.get().getPrix()).isEqualTo(100.0);
    }

    @Test
    void testGetAnnonceById_NotFound() {
        Optional<AnnonceDTO> result = annoncesService.getAnnonceById(999L);
        assertThat(result).isEmpty();
    }

    @Test
    void testCreateAnnonce() {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitre("Nouvelle Annonce");
        dto.setPrix(150.0);
        dto.setAdresse("456 New Street");
        dto.setVille("Yaoundé");
        dto.setLatitude(3.87);
        dto.setLongitude(11.52);
        dto.setNbreChambres(3);
        dto.setNbreLits(3);
        dto.setMaxInvites(6);
        dto.setDescription("Nouvelle description");
        dto.setTypeAnnonce("Maison");
        dto.setIdProprietaire(proprietaire.getId());

        AnnonceDTO created = annoncesService.createAnnonce(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitre()).isEqualTo("Nouvelle Annonce");
        assertThat(created.getEstActive()).isTrue();
        assertThat(created.getEvaluationMoyenne()).isEqualTo(0.0);
    }

    @Test
    void testCreateAnnonceAvecLocalisation() {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitre("Annonce avec localisation");
        dto.setPrix(200.0);
        dto.setAdresse("789 Loc Street");
        dto.setVille("Douala");
        dto.setNbreChambres(2);
        dto.setNbreLits(2);
        dto.setMaxInvites(4);
        dto.setDescription("Test");
        dto.setTypeAnnonce("Appartement");
        dto.setIdProprietaire(proprietaire.getId());

        LocalisationDTO locDTO = new LocalisationDTO();
        locDTO.setVille("Douala");
        locDTO.setQuartier("Bonanjo");
        locDTO.setLatitude(4.05);
        locDTO.setLongitude(9.70);
        dto.setLocalisation(locDTO);

        AnnonceDTO created = annoncesService.createAnnonce(dto);

        assertThat(created).isNotNull();
        Optional<LocalisationDTO> localisation = annoncesService.getLocalisation(created.getId());
        assertThat(localisation).isPresent();
        assertThat(localisation.get().getQuartier()).isEqualTo("Bonanjo");
    }

    @Test
    void testUpdateAnnonce() {
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

        AnnonceDTO updated = annoncesService.updateAnnonce(annonce.getId(), dto);

        assertThat(updated.getTitre()).isEqualTo("Titre Modifié");
        assertThat(updated.getPrix()).isEqualTo(250.0);
        assertThat(updated.getDescription()).isEqualTo("Description modifiée");
    }

    @Test
    void testUpdateAnnonce_NotFound() {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitre("Test");

        assertThatThrownBy(() -> annoncesService.updateAnnonce(999L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Annonce non trouvée");
    }

    @Test
    void testDeleteAnnonce() {
        annoncesService.deleteAnnonce(annonce.getId());

        Optional<Annonces> deleted = annoncesRepository.findById(annonce.getId());
        assertThat(deleted).isEmpty();
    }

    @Test
    void testActiverAnnonce() {
        annoncesService.activerAnnonce(annonce.getId(), false);

        Annonces updated = annoncesRepository.findById(annonce.getId()).orElseThrow();
        assertThat(updated.getEstActive()).isFalse();
    }

    @Test
    void testAddDisponibilite() {
        DisponibiliteDTO dto = new DisponibiliteDTO();
        dto.setIdAnnonce(annonce.getId());
        dto.setEstDisponible(true);
        dto.setDateDebut(new Date());
        dto.setDateFin(new Date(System.currentTimeMillis() + 86400000L * 7));
        dto.setPrixSurcharge(20.0);

        DisponibiliteDTO created = annoncesService.addDisponibilite(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getEstDisponible()).isTrue();
        assertThat(created.getPrixSurcharge()).isEqualTo(20.0);
    }

    @Test
    void testVerifierDisponibilite() {
        Disponibilite dispo = TestDataBuilder.createDisponibilite(annonce);
        disponibiliteRepository.save(dispo);

        Date debut = new Date();
        Date fin = new Date(System.currentTimeMillis() + 86400000L * 3);

        boolean disponible = annoncesService.verifierDisponibilite(annonce.getId(), debut, fin);

        assertThat(disponible).isTrue();
    }

    @Test
    void testUpdateLocalisation() {
        LocalisationDTO dto = new LocalisationDTO();
        dto.setVille("Yaoundé");
        dto.setQuartier("Bastos");
        dto.setLatitude(3.87);
        dto.setLongitude(11.52);

        LocalisationDTO updated = annoncesService.updateLocalisation(annonce.getId(), dto);

        assertThat(updated).isNotNull();
        assertThat(updated.getVille()).isEqualTo("Yaoundé");
        assertThat(updated.getQuartier()).isEqualTo("Bastos");
    }
}