package com.example.backend.authservice;



import com.example.backend.entityDTO.*;
import com.example.backend.entities.*;
import com.example.backend.repository.*;
import com.example.backend.roles.RoleUtilisateur;
import com.example.backend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

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

        if (userRepository.existsByEmail(dto.getEmail())) {
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

        switch (dto.getRole()) {
            case "VOYAGEUR":
                Voyageur voyageur = new Voyageur();
                voyageur.setIdUser(user);
                voyageur.setPreferences(dto.getPreferences());
                voyageur.setRoleUtilisateur(RoleUtilisateur.VOYAGEUR);
                voyageurRepository.save(voyageur);
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
                break;

            case "ADMIN":
                Admin admin = new Admin();
                admin.setIdUser(user);
                admin.setDepartement(dto.getDepartement());
                adminRepository.save(admin);
                break;

            default:
                throw new RuntimeException("Rôle invalide");
        }

        String token = jwtUtils.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

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

        Utilisateur user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(dto.getMotDePasse(), user.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        if (!user.getEstActif()) {
            throw new RuntimeException("Votre compte est désactivé");
        }

        String token = jwtUtils.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

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