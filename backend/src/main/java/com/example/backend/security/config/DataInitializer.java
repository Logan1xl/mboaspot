package com.example.backend.security.config;

import com.example.backend.repository.UserRepository;
import com.example.backend.repository.AdminRepository;
import com.example.backend.entities.Utilisateur;
import com.example.backend.entities.Admin;
import com.example.backend.roles.RoleUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Vérifie si l'admin existe déjà
        if (!userRepository.existsByEmail("admin@mboaspot.cm")) {

            System.out.println("🔧 Création de l'administrateur système...");

            // Crée l'utilisateur admin
            Utilisateur adminUser = new Utilisateur();
            adminUser.setNom("Admin");
            adminUser.setPrenom("Système");
            adminUser.setEmail("admin@mboaspot.cm");
            adminUser.setMotDePasse(passwordEncoder.encode("AdminPass123"));
            adminUser.setNumeroTelephone("237600000000");
            adminUser.setRole(RoleUtilisateur.ADMIN);
            adminUser.setEstActif(true);

            // Sauvegarde l'utilisateur
            adminUser = userRepository.save(adminUser);

            // Crée l'entrée dans la table admin
            Admin admin = new Admin();
            admin.setIdUser(adminUser);
            admin.setDepartement("Système");
            adminRepository.save(admin);

            System.out.println("✅ Administrateur créé avec succès !");
            System.out.println("   Email : admin@mboaspot.cm");
            System.out.println("   Mot de passe : AdminPass123");
            System.out.println("   ⚠️  CHANGEZ CE MOT DE PASSE EN PRODUCTION !");

        } else {
            System.out.println("ℹ️  Administrateur déjà existant");
        }
    }
}