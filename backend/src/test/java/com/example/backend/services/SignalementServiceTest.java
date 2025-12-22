package com.example.backend.services;

import com.example.backend.dto.SignalementRequestDTO;
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
class SignalementServiceTest {

    @Autowired
    private SignalementService signalementService;

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
        // Nettoyage dans le bon ordre (dépendances d'abord)
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

        // Créer admin (utilisateur + entité admin)
        Utilisateur userAdmin = TestDataBuilder.createUtilisateur("Admin", "admin@test.com");
        userAdmin.setRole("ADMIN");
        userAdmin = utilisateurRepository.save(userAdmin);

        admin = TestDataBuilder.createAdmin(userAdmin);
        admin = adminRepository.save(admin);

        // Créer signalement
        signalement = TestDataBuilder.createSignalement(annonce, admin);
        signalement = signalementRepository.save(signalement);
    }

    @Test
    void testCreer() {
        // Créer un nouvel admin pour ce test
        Utilisateur userAdmin2 = TestDataBuilder.createUtilisateur("Admin2", "admin2@test.com");
        userAdmin2.setRole("ADMIN");
        userAdmin2 = utilisateurRepository.save(userAdmin2);

        Admin admin2 = TestDataBuilder.createAdmin(userAdmin2);
        admin2 = adminRepository.save(admin2);

        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(admin2.getId());
        dto.setRaison("Fausse information");
        dto.setDescription("Les photos ne correspondent pas à la réalité");

        Signalement created = signalementService.creer(dto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getRaison()).isEqualTo("Fausse information");
        assertThat(created.getDescription()).isEqualTo("Les photos ne correspondent pas à la réalité");
        assertThat(created.getStatut()).isEqualTo("EN_ATTENTE");
        assertThat(created.getIdAnnonce().getId()).isEqualTo(annonce.getId());
        assertThat(created.getIdAdmin().getId()).isEqualTo(admin2.getId());
    }

    @Test
    void testCreer_AnnonceInexistante() {
        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(999L);
        dto.setAdminId(admin.getId());
        dto.setRaison("Test");

        assertThatThrownBy(() -> signalementService.creer(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Annonce non trouvée");
    }

    @Test
    void testCreer_AdminInexistant() {
        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(999L);
        dto.setRaison("Test");

        assertThatThrownBy(() -> signalementService.creer(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Admin introuvable");
    }

    @Test
    void testGetAll() {
        // Créer un deuxième signalement
        Utilisateur userAdmin2 = TestDataBuilder.createUtilisateur("Admin3", "admin3@test.com");
        userAdmin2 = utilisateurRepository.save(userAdmin2);
        Admin admin2 = TestDataBuilder.createAdmin(userAdmin2);
        admin2 = adminRepository.save(admin2);

        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(admin2.getId());
        dto.setRaison("Autre signalement");
        dto.setDescription("Test");
        signalementService.creer(dto);

        List<Signalement> signalements = signalementService.getAll();

        assertThat(signalements).isNotEmpty();
        assertThat(signalements).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetById() {
        Signalement found = signalementService.getById(signalement.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(signalement.getId());
        assertThat(found.getRaison()).isEqualTo("Contenu inapproprié");
        assertThat(found.getIdAdmin()).isNotNull();
        assertThat(found.getIdAnnonce()).isNotNull();
    }

    @Test
    void testGetById_NotFound() {
        assertThatThrownBy(() -> signalementService.getById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Signalement non trouvé");
    }

    @Test
    void testTraiter() {
        Signalement traite = signalementService.traiter(
                signalement.getId(),
                "TRAITE",
                "Annonce désactivée suite à vérification"
        );

        assertThat(traite).isNotNull();
        assertThat(traite.getStatut()).isEqualTo("TRAITE");
        assertThat(traite.getResolution()).isEqualTo("Annonce désactivée suite à vérification");
    }

    @Test
    void testTraiter_SignalementRejete() {
        Signalement traite = signalementService.traiter(
                signalement.getId(),
                "REJETE",
                "Signalement infondé après vérification"
        );

        assertThat(traite.getStatut()).isEqualTo("REJETE");
        assertThat(traite.getResolution()).contains("infondé");
    }

    @Test
    void testSupprimer() {
        Long signalementId = signalement.getId();

        signalementService.supprimer(signalementId);

        assertThat(signalementRepository.existsById(signalementId)).isFalse();
    }

    @Test
    void testWorkflow_CompletSignalement() {
        // 1. Créer un nouvel admin
        Utilisateur userAdmin2 = TestDataBuilder.createUtilisateur("AdminWorkflow", "adminworkflow@test.com");
        userAdmin2 = utilisateurRepository.save(userAdmin2);
        Admin admin2 = TestDataBuilder.createAdmin(userAdmin2);
        admin2 = adminRepository.save(admin2);

        // 2. Créer un nouveau signalement
        SignalementRequestDTO dto = new SignalementRequestDTO();
        dto.setAnnonceId(annonce.getId());
        dto.setAdminId(admin2.getId());
        dto.setRaison("Prix suspect");
        dto.setDescription("Le prix semble trop bas par rapport au marché");

        Signalement nouveau = signalementService.creer(dto);
        assertThat(nouveau.getStatut()).isEqualTo("EN_ATTENTE");

        // 3. Le récupérer
        Signalement recupere = signalementService.getById(nouveau.getId());
        assertThat(recupere).isNotNull();
        assertThat(recupere.getRaison()).isEqualTo("Prix suspect");

        // 4. Le traiter
        Signalement traite = signalementService.traiter(
                nouveau.getId(),
                "TRAITE",
                "Prix vérifié et corrigé"
        );
        assertThat(traite.getStatut()).isEqualTo("TRAITE");
        assertThat(traite.getResolution()).isEqualTo("Prix vérifié et corrigé");

        // 5. Vérifier qu'il est dans la liste
        List<Signalement> tous = signalementService.getAll();
        assertThat(tous).extracting(Signalement::getId).contains(nouveau.getId());
    }
}
