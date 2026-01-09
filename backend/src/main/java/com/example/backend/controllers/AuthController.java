package com.example.backend.controllers;

import com.example.backend.dto.RefreshTokenDTO;
import com.example.backend.entities.Utilisateur;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.services.implementations.AuthService;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        logger.info("Tentative d'inscription pour email={}", dto.getEmail());

        if ("ADMIN".equalsIgnoreCase(dto.getRole())) {
            logger.warn("Inscription refusée pour rôle ADMIN email={}", dto.getEmail());
            throw new RuntimeException("Impossible de s'inscrire en tant qu'administrateur");
        }

        AuthResponseDTO response = authService.register(dto);
        logger.info("Inscription réussie pour email={}", dto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        logger.info("Tentative de login pour email={}", dto.getEmail());

        AuthResponseDTO response = authService.login(dto);

        logger.info("Login réussi pour email={}", dto.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map> refreshToken(@RequestBody RefreshTokenDTO dto) {
        logger.info("Demande de refresh token");

        try {
            String newToken = jwtUtils.generateTokenFromRefreshToken(dto.getRefreshToken());

            Map<String, String> response = new HashMap<>();
            response.put("token", newToken);

            logger.info("Refresh token réussi");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.warn("Refresh token échoué", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
