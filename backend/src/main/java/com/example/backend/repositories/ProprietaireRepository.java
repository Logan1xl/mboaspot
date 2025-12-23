package com.example.backend.repositories;

import com.example.backend.entities.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProprietaireRepository extends JpaRepository<Proprietaire, Long> {

    // Trouver un propriétaire par son utilisateur
    Optional<Proprietaire> findByIdUserId(Long userId);

    // Trouver par numéro d'identification
    Optional<Proprietaire> findByNumeroIdentification(String numeroIdentification);
}