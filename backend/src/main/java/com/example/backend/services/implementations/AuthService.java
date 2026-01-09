package com.example.backend.services.implementations;

import com.example.backend.entities.*;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.RegisterDTO;
import com.example.backend.repositories.AdminRepository;
import com.example.backend.repositories.*;
import com.example.backend.roles.RoleUtilisateur;
import com.example.backend.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private VoyageurRepository voyageurRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public AuthResponseDTO register(RegisterDTO dto) {

        log.info("Tentative d'inscription avec email={} et role={}", dto.getEmail(), dto.getRole());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Échec inscription : email déjà utilisé [{}]", dto.getEmail());
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        user.setNumeroTelephone(dto.getNumeroTelephone());
        user.setPhotoProfil(dto.getPhotoProfil());
        user.setRole(RoleUtilisateur.valueOf(dto.getRole()));
        user.setEstActif(true);

        user = userRepository.save(user);

        log.info("Utilisateur créé avec succès id={} email={}", user.getId(), user.getEmail());

        switch (dto.getRole()) {

            case "VOYAGEUR":
                Voyageur voyageur = new Voyageur();
                voyageur.setIdUser(user);
                voyageur.setPreferences(dto.getPreferences());
                voyageur.setRoleUtilisateur(RoleUtilisateur.VOYAGEUR);
                voyageurRepository.save(voyageur);
                log.info("Profil VOYAGEUR créé pour userId={}", user.getId());
                break;

            case "PROPRIETAIRE":
                Proprietaire proprietaire = new Proprietaire();
                proprietaire.setIdUser(user);
                proprietaire.setNomEntreprise(dto.getNomEntreprise());
                proprietaire.setNumeroIdentification(dto.getNumeroIdentification());
                proprietaire.setCompteBancaire(dto.getCompteBancaire());
                proprietaire.setGainsTotal(0.0);
                proprietaire.setTotalAnnonces(0);
                proprietaire.setEvaluationMoyenne(0.0);
                proprietaireRepository.save(proprietaire);
                log.info("Profil PROPRIETAIRE créé pour userId={}", user.getId());
                break;

            case "ADMIN":
                Admin admin = new Admin();
                admin.setIdUser(user);
                adminRepository.save(admin);
                log.info("Profil ADMIN créé pour userId={}", user.getId());
                break;

            default:
                log.error("Rôle invalide lors de l'inscription : {}", dto.getRole());
                throw new RuntimeException("Rôle invalide");
        }

        String token = jwtUtils.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        log.info("Inscription réussie pour userId={} role={}", user.getId(), user.getRole());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setRole(user.getRole());
        response.setNumeroTelephone(user.getNumeroTelephone());
        response.setPhotoProfil(user.getPhotoProfil());

        return response;
    }

    public AuthResponseDTO login(LoginDTO dto) {

        log.info("Tentative de connexion avec email={}", dto.getEmail());

        Utilisateur user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Échec connexion : email inexistant [{}]", dto.getEmail());
                    return new RuntimeException("Email ou mot de passe incorrect");
                });

        if (!passwordEncoder.matches(dto.getMotDePasse(), user.getMotDePasse())) {
            log.warn("Échec connexion : mot de passe incorrect [{}]", dto.getEmail());
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        if (user.getEstActif() == null || !user.getEstActif()) {
            log.warn("Connexion refusée : compte désactivé userId={}", user.getId());
            throw new RuntimeException("Votre compte est désactivé");
        }

        String token = jwtUtils.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        log.info("Connexion réussie userId={} role={}", user.getId(), user.getRole());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setRole(user.getRole());
        response.setNumeroTelephone(user.getNumeroTelephone());
        response.setPhotoProfil(user.getPhotoProfil());

        return response;
    }
}
