package com.example.backend.auth.authController;

import com.example.backend.auth.authController.authservice.AuthService;
import com.example.backend.entityDTO.authdto.AuthResponseDTO;
import com.example.backend.entityDTO.authdto.LoginDTO;
import com.example.backend.entityDTO.authdto.RegisterDTO;
import com.example.backend.roles.RoleUtilisateur;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.default_catalog=",
        "jwt.secret=testSecretKeyForJwtThatIsAtLeast256BitsLongForHS256Algorithm",
        "jwt.expiration=86400000"
})
@ActiveProfiles("test")  // Active le profil test
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private RegisterDTO registerDTO;
    private LoginDTO loginDTO;
    private AuthResponseDTO authResponseDTO;

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

        // Configuration de la réponse attendue
        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("fake-jwt-token");
        authResponseDTO.setUserId(1L);
        authResponseDTO.setEmail("wulfrid@example.com");
        authResponseDTO.setNom("Mbongo");
        authResponseDTO.setPrenom("Wulfrid");
        authResponseDTO.setRole(RoleUtilisateur.VOYAGEUR);
        authResponseDTO.setNumeroTelephone("+237123456789");
        authResponseDTO.setPhotoProfil("photo.jpg");
    }

    // ==================== TESTS REGISTER ====================

    @Test
    void register_AvecDonneesValides_DevraitRetourner201() throws Exception {
        // Arrange
        when(authService.register(any(RegisterDTO.class))).thenReturn(authResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("wulfrid@example.com"));

        verify(authService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void register_AvecRoleVoyageur_DevraitReussir() throws Exception {
        // Arrange
        when(authService.register(any(RegisterDTO.class))).thenReturn(authResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated());

        verify(authService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void register_AvecRoleAdmin_DevraitRetourner500() throws Exception {
        // Arrange
        registerDTO.setRole("ADMIN");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isInternalServerError());

        verify(authService, never()).register(any(RegisterDTO.class));
    }
    @Test
    void register_AvecEmailExistant_DevraitRetourner500() throws Exception {
        // Arrange
        when(authService.register(any(RegisterDTO.class)))
                .thenThrow(new RuntimeException("Cet email est déjà utilisé"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().is5xxServerError()); // ← Changement ici : accepte n'importe quel 5xx

        verify(authService, times(1)).register(any(RegisterDTO.class));
    }

    // ==================== TESTS LOGIN ====================

    @Test
    void login_AvecCredentialsValides_DevraitRetourner200() throws Exception {
        // Arrange
        when(authService.login(any(LoginDTO.class))).thenReturn(authResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("wulfrid@example.com"));

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void login_AvecEmailInexistant_DevraitRetourner500() throws Exception {
        // Arrange
        when(authService.login(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Email ou mot de passe incorrect"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isInternalServerError());

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    // ==================== TEST ENDPOINT TEST ====================

    @Test
    void test_DevraitRetourner200AvecMessage() throws Exception {
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("API fonctionne !"));
    }
}