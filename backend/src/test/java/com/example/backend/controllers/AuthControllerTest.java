package com.example.backend.controllers;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.RegisterDTO;
import com.example.backend.services.implementations.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;





@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_DevraitRetournerAuthResponse_QuandInscriptionReussie() throws Exception {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setMotDePasse("password123");
        registerDTO.setNom("Doe");
        registerDTO.setPrenom("John");
        registerDTO.setNumeroTelephone("0123456789");
        registerDTO.setRole("PROPRIETAIRE");

        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setToken("jwt-token");
        authResponse.setEmail("test@example.com");

        when(authService.register(any(RegisterDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(authService, times(1)).register(any(RegisterDTO.class));
    }

    @Test
    void register_DevraitLancerException_QuandRoleEstAdmin() throws Exception {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("admin@example.com");
        registerDTO.setMotDePasse("admin123");
        registerDTO.setNumeroTelephone("0123456789");
        registerDTO.setNom("Admin");
        registerDTO.setPrenom("Super");
        registerDTO.setRole("ADMIN");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Impossible de s'inscrire en tant qu'administrateur"));

        verify(authService, never()).register(any(RegisterDTO.class));
    }

    @Test
    void login_DevraitRetournerAuthResponse_QuandConnexionReussie() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setMotDePasse("password123");

        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setToken("jwt-token");
        authResponse.setEmail("test@example.com");

        when(authService.login(any(LoginDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void login_DevraitRetournerErreur_QuandCredentialsInvalides() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("wrong@example.com");
        loginDTO.setMotDePasse("wrongpass");

        when(authService.login(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Identifiants invalides"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Identifiants invalides"));

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void test_DevraitRetournerMessageSucces() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("API fonctionne !"));
    }
}