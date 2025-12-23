package com.example.backend.repositories;

import com.example.backend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des utilisateurs (Mock pour la compilation)
 * @author Suzanne Lumiere
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Méthodes de recherche personnalisées si nécessaire

    
}
