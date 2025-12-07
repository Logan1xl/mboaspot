package com.example.backend.authController;

import com.example.backend.entityDTO.*;
import com.example.backend.authservice.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint d'inscription
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {

        // Bloque la création d'ADMIN via cet endpoint public
        if ("ADMIN".equalsIgnoreCase(dto.getRole())) {
            throw new RuntimeException("Impossible de s'inscrire en tant qu'administrateur");
        }

        AuthResponseDTO response = authService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    /**
     * Endpoint de connexion
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dto) {
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de test (pour vérifier que l'API fonctionne)
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API fonctionne !");
    }
}