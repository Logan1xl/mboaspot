
package com.example.backend.config;

import com.example.backend.security.jwt.JwtFilter;
import com.example.backend.security.service.CustomUserDetailsService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité pour les tests
 * Mock tous les composants de sécurité nécessaires
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    /**
     * Mock du JwtFilter pour éviter les problèmes d'injection
     */
    @MockBean
    private JwtFilter jwtFilter;

    /**
     * Mock du CustomUserDetailsService
     */
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configuration du SecurityFilterChain pour les tests
     * Désactive complètement la sécurité
     */
    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /**
     * Encoder de mots de passe pour les tests
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}