package com.example.backend.services;

import com.example.backend.dto.*;
import com.example.backend.entities.*;
import com.example.backend.repositories.*;
import com.example.backend.roles.RoleUtilisateur;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.services.implementations.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

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
        // Initialisation du RegisterDTO
        registerDTO = new RegisterDTO();
        registerDTO.setNom("Doe");
        registerDTO.setPrenom("John");
        registerDTO.setEmail("john@example.com");
        registerDTO.setMotDePasse("password123");
        registerDTO.setNumeroTelephone("0123456789");
        registerDTO.setRole("VOYAGEUR");

        // Initialisation du LoginDTO
        loginDTO = new LoginDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setMotDePasse("password123");

        // Initialisation de l'utilisateur
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setNom("Doe");
        utilisateur.setPrenom("John");
        utilisateur.setEmail("john@example.com");
        utilisateur.setMotDePasse("encodedPassword");
        utilisateur.setRole(RoleUtilisateur.VOYAGEUR);
        utilisateur.setEstActif(true);
    }

    @Test
    void register_VoyageurSuccess() {
        // Given
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getMotDePasse())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(voyageurRepository.save(any(Voyageur.class))).thenReturn(new Voyageur());
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("token123");

        // When
        AuthResponseDTO result = authService.register(registerDTO);

        // Then
        assertNotNull(result);
        assertEquals("token123", result.getToken());
        assertEquals(1L, result.getUserId());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("Doe", result.getNom());
        assertEquals("John", result.getPrenom());

        verify(userRepository).existsByEmail(registerDTO.getEmail());
        verify(userRepository).save(any(Utilisateur.class));
        verify(voyageurRepository).save(any(Voyageur.class));
        verify(jwtUtils).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void register_ProprietaireSuccess() {
        // Given
        registerDTO.setRole("PROPRIETAIRE");
        registerDTO.setNomEntreprise("Entreprise Test");
        registerDTO.setNumeroIdentification("123456");

        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getMotDePasse())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
        when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(new Proprietaire());
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("token123");

        // When
        AuthResponseDTO result = authService.register(registerDTO);

        // Then
        assertNotNull(result);
        assertEquals("token123", result.getToken());

        verify(userRepository).existsByEmail(registerDTO.getEmail());
        verify(userRepository).save(any(Utilisateur.class));
        verify(proprietaireRepository).save(any(Proprietaire.class));
    }

    @Test
    void register_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerDTO);
        });

        assertEquals("Cet email est déjà utilisé", exception.getMessage());

        verify(userRepository).existsByEmail(registerDTO.getEmail());
        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void register_InvalidRole() {
        // Given
        registerDTO.setRole("INVALID_ROLE");

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authService.register(registerDTO);
        });
    }


    @Test
    void login_Success() {
        // Given
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse())).thenReturn(true);
        when(jwtUtils.generateToken(anyString(), anyString(), anyLong())).thenReturn("token123");

        // When
        AuthResponseDTO result = authService.login(loginDTO);

        // Then
        assertNotNull(result);
        assertEquals("token123", result.getToken());
        assertEquals(1L, result.getUserId());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository).findByEmail(loginDTO.getEmail());
        verify(passwordEncoder).matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse());
        verify(jwtUtils).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_UserNotFound() {
        // Given
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("Email ou mot de passe incorrect", exception.getMessage());

        verify(userRepository).findByEmail(loginDTO.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_IncorrectPassword() {
        // Given
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("Email ou mot de passe incorrect", exception.getMessage());

        verify(userRepository).findByEmail(loginDTO.getEmail());
        verify(passwordEncoder).matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    void login_InactiveAccount() {
        // Given
        utilisateur.setEstActif(false);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("Votre compte est désactivé", exception.getMessage());

        verify(userRepository).findByEmail(loginDTO.getEmail());
        verify(passwordEncoder).matches(loginDTO.getMotDePasse(), utilisateur.getMotDePasse());
        verify(jwtUtils, never()).generateToken(anyString(), anyString(), anyLong());
    }
}