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
import com.example.backend.security.jwt.JwtUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        if ("ADMIN".equalsIgnoreCase(dto.getRole())) {
            throw new RuntimeException("Impossible de s'inscrire en tant qu'administrateur");
        }
        AuthResponseDTO response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map> refreshToken(@RequestBody RefreshTokenDTO dto) {
        try {
            String newToken = jwtUtils.generateTokenFromRefreshToken(dto.getRefreshToken());

            Map response = new HashMap<>();
            response.put("token", newToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
}