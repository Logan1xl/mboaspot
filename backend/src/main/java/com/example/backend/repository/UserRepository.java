package com.example.backend.repository;

import com.example.backend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {

        Optional<Utilisateur> findByEmail(String email);

        boolean existsByEmail(String email);
    }


