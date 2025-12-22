package com.example.backend.auth.authController.authservice;

import com.example.backend.entities.*;
import com.example.backend.entityDTO.authdto.AuthResponseDTO;
import com.example.backend.entityDTO.authdto.LoginDTO;
import com.example.backend.entityDTO.authdto.RegisterDTO;
import com.example.backend.repository.*;
import com.example.backend.roles.RoleUtilisateur;
import com.example.backend.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoyageurRepository voyageurRepository;

    @Mock
    private ProprietaireRepository proprietaireRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        // Configuration du RegisterDTO
        registerDTO = new RegisterDTO();
        registerDTO.setNom("Mbongo");
        registerDTO.setPrenom("Wulfrid");
        registerDTO.setEmail("wulfrid@example.com");
        registerDTO.setMotDePasse("password123");
        registerDTO.setNumeroTelephone("+237123456789");
        registerDTO.setPhotoProfil("photo.jpg");
        registerDTO.setRole("VOYAGEUR");
        registerDTO.setPreferences("Préférences test");

        // Configuration du LoginDTO
        loginDTO = new LoginDTO();
        loginDTO.setEmail("wulfrid@example.com");
        loginDTO.setMotDePasse("password123");

        // Configuration de l'utilisateur
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNom("Mbongo");
        utilisateur.setPrenom("Wulfrid");
        utilisateur.setEmail("wulfrid@example.com");
        utilisateur.setMotDePasse("encodedPassword");
        utilisateur.setNumeroTelephone("+237123456789");
        utilisateur.setPhotoProfil("photo.jpg");
        utilisateur.setRole(RoleUtilisateur.VOYAGEUR);
        utilisateur.setEstActif(true);
    }

    // ==================== TESTS REGISTER ====================

    @Test
    void register_AvecVoyageur_DevraitCreerUtilisateurEtVoyageur() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authService.register(registerDTO);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("wulfrid@example.com", response.getEmail());
        assertEquals("Mbongo", response.getNom());
        assertEquals("Wulfrid", response.getPrenom());
        assertEquals(RoleUtilisateur.VOYAGEUR, response.getRole());

        // Vérifications des appels
        verify(userRepository).existsByEmail("wulfrid@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(Utilisateur.class));
        verify(voyageurRepository).save(any(Voyageur.class));
        verify(jwtUtils).generateToken("wulfrid@example.com", "VOYAGEUR", 1L);

        // Vérifier que les autres repositories ne sont pas appelés
        verify(proprietaireRepository, never()).save(any(Proprietaire.class));
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void register_AvecProprietaire_DevraitCreerUtilisateurEtProprietaire() {
        // Arrange
        registerDTO.setRole("PROPRIETAIRE");
        registerDTO.setNomEntreprise("Entreprise Test");
        registerDTO.setNumeroIdentification("ID123456");
        registerDTO.setCompteBancaire("CM1234567890");

        utilisateur.setRole(RoleUtilisateur.PROPRIETAIRE);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authService.register(registerDTO);

        // Assert
        assertNotNull(response);
        assertEquals(RoleUtilisateur.PROPRIETAIRE, response.getRole());

        // Vérifier que le propriétaire a été créé avec les bonnes valeurs
        ArgumentCaptor<Proprietaire> proprietaireCaptor = ArgumentCaptor.forClass(Proprietaire.class);
        verify(proprietaireRepository).save(proprietaireCaptor.capture());

        Proprietaire savedProprietaire = proprietaireCaptor.getValue();
        assertEquals("Entreprise Test", savedProprietaire.getNomEntreprise());
        assertEquals("ID123456", savedProprietaire.getNumeroIdentification());
        assertEquals("CM1234567890", savedProprietaire.getCompteBancaire());
        assertEquals(0.0, savedProprietaire.getGainsTotal());
        assertEquals(0, savedProprietaire.getTotalAnnonces());
        assertEquals(0.0, savedProprietaire.getEvaluationMoyenne());

        verify(voyageurRepository, never()).save(any(Voyageur.class));
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void register_AvecAdmin_DevraitCreerUtilisateurEtAdmin() {
        // Arrange
        registerDTO.setRole("ADMIN");
        utilisateur.setRole(RoleUtilisateur.ADMIN);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authService.register(registerDTO);

        // Assert
        assertNotNull(response);
        assertEquals(RoleUtilisateur.ADMIN, response.getRole());

        verify(adminRepository).save(any(Admin.class));
        verify(voyageurRepository, never()).save(any(Voyageur.class));
        verify(proprietaireRepository, never()).save(any(Proprietaire.class));
    }

    @Test
    void register_AvecEmailExistant_DevraitLancerException() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(registerDTO));

        assertEquals("Cet email est déjà utilisé", exception.getMessage());

        verify(userRepository).existsByEmail("wulfrid@example.com");
        verify(userRepository, never()).save(any(Utilisateur.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void register_AvecRoleInvalide_DevraitLancerException() {
        // Arrange
        registerDTO.setRole("ROLE_INVALIDE");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        // Pas besoin de mock userRepository.save() car l'exception est lancée avant

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authService.register(registerDTO));

        verify(userRepository).existsByEmail("wulfrid@example.com");
    }

    @Test
    void register_DevraitEncoderLeMotDePasse() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        authService.register(registerDTO);

        // Assert
        ArgumentCaptor<Utilisateur> userCaptor = ArgumentCaptor.forClass(Utilisateur.class);
        verify(userRepository).save(userCaptor.capture());

        Utilisateur savedUser = userCaptor.getValue();
        assertEquals("encodedPassword", savedUser.getMotDePasse());
        verify(passwordEncoder).encode("password123");
    }

    @Test
    void register_DevraitDefinirEstActifATrue() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        authService.register(registerDTO);

        // Assert
        ArgumentCaptor<Utilisateur> userCaptor = ArgumentCaptor.forClass(Utilisateur.class);
        verify(userRepository).save(userCaptor.capture());

        Utilisateur savedUser = userCaptor.getValue();
        assertTrue(savedUser.getEstActif());
    }

    // ==================== TESTS LOGIN ====================

    @Test
    void login_AvecCredentialsValides_DevraitRetournerAuthResponse() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authService.login(loginDTO);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("wulfrid@example.com", response.getEmail());
        assertEquals("Mbongo", response.getNom());
        assertEquals("Wulfrid", response.getPrenom());
        assertEquals(RoleUtilisateur.VOYAGEUR, response.getRole());
        assertEquals("+237123456789", response.getNumeroTelephone());
        assertEquals("photo.jpg", response.getPhotoProfil());

        verify(userRepository).findByEmail("wulfrid@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtils).generateToken("wulfrid@example.com", "VOYAGEUR", 1L);
    }

    @Test
    void login_AvecEmailInexistant_DevraitLancerException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginDTO));

        assertEquals("Email ou mot de passe incorrect", exception.getMessage());

        verify(userRepository).findByEmail("wulfrid@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_AvecMotDePasseIncorrect_DevraitLancerException() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginDTO));

        assertEquals("Email ou mot de passe incorrect", exception.getMessage());

        verify(userRepository).findByEmail("wulfrid@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_AvecCompteDesactive_DevraitLancerException() {
        // Arrange
        utilisateur.setEstActif(false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginDTO));

        assertEquals("Votre compte est désactivé", exception.getMessage());

        verify(userRepository).findByEmail("wulfrid@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_AvecEstActifNull_DevraitLancerException() {
        // Arrange
        utilisateur.setEstActif(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginDTO));

        assertEquals("Votre compte est désactivé", exception.getMessage());
    }

    @Test
    void login_DevraitGenererTokenAvecBonnesInformations() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        authService.login(loginDTO);

        // Assert
        verify(jwtUtils).generateToken(
                "wulfrid@example.com",
                "VOYAGEUR",
                1L
        );
    }

    // ==================== TESTS D'INTÉGRATION ====================

    @Test
    void register_PuisLogin_DevraitFonctionner() {
        // Arrange - Register
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("register-token");

        // Act - Register
        AuthResponseDTO registerResponse = authService.register(registerDTO);

        // Assert - Register
        assertNotNull(registerResponse);
        assertEquals("register-token", registerResponse.getToken());

        // Arrange - Login
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("login-token");

        // Act - Login
        AuthResponseDTO loginResponse = authService.login(loginDTO);

        // Assert - Login
        assertNotNull(loginResponse);
        assertEquals("login-token", loginResponse.getToken());
        assertEquals(registerResponse.getUserId(), loginResponse.getUserId());
        assertEquals(registerResponse.getEmail(), loginResponse.getEmail());
    }

    @Test
    void register_AvecTousLesChamps_DevraitConserverToutesLesInformations() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("fake-jwt-token");

        // Act
        AuthResponseDTO response = authService.register(registerDTO);

        // Assert
        ArgumentCaptor<Utilisateur> userCaptor = ArgumentCaptor.forClass(Utilisateur.class);
        verify(userRepository).save(userCaptor.capture());

        Utilisateur savedUser = userCaptor.getValue();
        assertEquals("Mbongo", savedUser.getNom());
        assertEquals("Wulfrid", savedUser.getPrenom());
        assertEquals("wulfrid@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getMotDePasse());
        assertEquals("+237123456789", savedUser.getNumeroTelephone());
        assertEquals("photo.jpg", savedUser.getPhotoProfil());
        assertEquals(RoleUtilisateur.VOYAGEUR, savedUser.getRole());
        assertTrue(savedUser.getEstActif());
    }
}