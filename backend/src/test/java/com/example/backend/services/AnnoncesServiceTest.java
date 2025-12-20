package com.example.backend.services;

import com.example.backend.dto.AnnonceDTO;
import com.example.backend.dto.DisponibiliteDTO;
import com.example.backend.dto.LocalisationDTO;
import com.example.backend.dto.RechercheDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.repositories.AnnoncesRepository;
import com.example.backend.repositories.DisponibiliteRepository;
import com.example.backend.repositories.LocalisationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AnnoncesServiceTest {

    @Autowired
    private AnnoncesService annoncesService;

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private LocalisationRepository localisationRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    private AnnonceDTO annonceTest;

    @BeforeEach
    void setUp() {
        // Nettoyer la base
        disponibiliteRepository.deleteAll();
        localisationRepository.deleteAll();
        annoncesRepository.deleteAll();

        // Créer une annonce de test
        annonceTest = new AnnonceDTO();
        annonceTest.setTitre("Villa luxueuse");
        annonceTest.setPrix(100000.0);
        annonceTest.setAdresse("456 Avenue Test");
        annonceTest.setVille("Douala");
        annonceTest.setLatitude(4.0511);
        annonceTest.setLongitude(9.7679);
        annonceTest.setNbreChambres(4);
        annonceTest.setNbreLits(3);
        annonceTest.setMaxInvites(8);
        annonceTest.setDescription("Belle villa avec piscine");
        annonceTest.setTypeAnnonce("MAISON");
        annonceTest.setUrlImagePrincipale("http://example.com/villa.jpg");
        annonceTest.setUrlImages(Arrays.asList("img1.jpg", "img2.jpg", "img3.jpg"));
    }

    @Test
    void testCreateAnnonce() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitre()).isEqualTo("Villa luxueuse");
        assertThat(created.getPrix()).isEqualTo(100000.0);
        assertThat(created.getEstActive()).isTrue();
        assertThat(created.getEvaluationMoyenne()).isEqualTo(0.0);
    }

    @Test
    void testGetAllAnnonces() {
        annoncesService.createAnnonce(annonceTest);

        List<AnnonceDTO> annonces = annoncesService.getAllAnnonces();

        assertThat(annonces).isNotEmpty();
        assertThat(annonces).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void testGetAnnoncesActives() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        List<AnnonceDTO> actives = annoncesService.getAnnoncesActives();

        assertThat(actives).isNotEmpty();
        assertThat(actives).allMatch(a -> a.getEstActive());
    }

    @Test
    void testGetAnnonceById() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        Optional<AnnonceDTO> found = annoncesService.getAnnonceById(created.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitre()).isEqualTo("Villa luxueuse");
    }

    @Test
    void testGetAnnonceById_NotFound() {
        Optional<AnnonceDTO> found = annoncesService.getAnnonceById(99999L);

        assertThat(found).isEmpty();
    }

    @Test
    void testUpdateAnnonce() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        created.setTitre("Villa modifiée");
        created.setPrix(120000.0);
        created.setNbreChambres(5);

        AnnonceDTO updated = annoncesService.updateAnnonce(created.getId(), created);

        assertThat(updated.getTitre()).isEqualTo("Villa modifiée");
        assertThat(updated.getPrix()).isEqualTo(120000.0);
        assertThat(updated.getNbreChambres()).isEqualTo(5);
    }

    @Test
    void testUpdateAnnonce_NotFound() {
        assertThatThrownBy(() -> {
            annoncesService.updateAnnonce(99999L, annonceTest);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("non trouvée");
    }

    @Test
    void testDeleteAnnonce() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);
        Long id = created.getId();

        annoncesService.deleteAnnonce(id);

        Optional<AnnonceDTO> found = annoncesService.getAnnonceById(id);
        assertThat(found).isEmpty();
    }

    @Test
    void testActiverAnnonce() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        annoncesService.activerAnnonce(created.getId(), false);

        Optional<AnnonceDTO> found = annoncesService.getAnnonceById(created.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEstActive()).isFalse();
    }

    @Test
    void testRechercherAnnonces_ParVille() {
        annoncesService.createAnnonce(annonceTest);

        RechercheDTO recherche = new RechercheDTO();
        recherche.setVille("Douala");

        List<AnnonceDTO> resultats = annoncesService.rechercherAnnonces(recherche);

        assertThat(resultats).isNotEmpty();
        assertThat(resultats).allMatch(a -> "Douala".equals(a.getVille()));
    }

    @Test
    void testRechercherAnnonces_ParPrix() {
        annoncesService.createAnnonce(annonceTest);

        RechercheDTO recherche = new RechercheDTO();
        recherche.setPrixMin(50000.0);
        recherche.setPrixMax(150000.0);

        List<AnnonceDTO> resultats = annoncesService.rechercherAnnonces(recherche);

        assertThat(resultats).isNotEmpty();
        assertThat(resultats).allMatch(a ->
                a.getPrix() >= 50000.0 && a.getPrix() <= 150000.0
        );
    }

    @Test
    void testAddDisponibilite() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        DisponibiliteDTO dispo = new DisponibiliteDTO();
        dispo.setIdAnnonce(created.getId());
        dispo.setEstDisponible(true);
        dispo.setPrixSurcharge(5000.0);
        dispo.setDateDebut(new Date());
        dispo.setDateFin(new Date(System.currentTimeMillis() + 86400000)); // +1 jour

        DisponibiliteDTO added = annoncesService.addDisponibilite(dispo);

        assertThat(added).isNotNull();
        assertThat(added.getId()).isNotNull();
        assertThat(added.getEstDisponible()).isTrue();
    }

    @Test
    void testGetDisponibilites() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        DisponibiliteDTO dispo = new DisponibiliteDTO();
        dispo.setIdAnnonce(created.getId());
        dispo.setEstDisponible(true);
        dispo.setDateDebut(new Date());
        dispo.setDateFin(new Date(System.currentTimeMillis() + 86400000));
        annoncesService.addDisponibilite(dispo);

        List<DisponibiliteDTO> disponibilites = annoncesService.getDisponibilites(created.getId());

        assertThat(disponibilites).isNotEmpty();
        assertThat(disponibilites).hasSize(1);
    }

    @Test
    void testUpdateLocalisation() {
        AnnonceDTO created = annoncesService.createAnnonce(annonceTest);

        LocalisationDTO localisation = new LocalisationDTO();
        localisation.setVille("Yaoundé");
        localisation.setQuartier("Bastos");
        localisation.setLatitude(3.8480);
        localisation.setLongitude(11.5021);

        LocalisationDTO updated = annoncesService.updateLocalisation(created.getId(), localisation);

        assertThat(updated).isNotNull();
        assertThat(updated.getVille()).isEqualTo("Yaoundé");
        assertThat(updated.getQuartier()).isEqualTo("Bastos");
    }

    @Test
    void testGetVillesDisponibles() {
        annoncesService.createAnnonce(annonceTest);

        List<String> villes = annoncesService.getVillesDisponibles();

        assertThat(villes).isNotNull();
    }
}