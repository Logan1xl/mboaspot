package com.example.backend.repositories;

import com.example.backend.entities.Annonces;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnoncesRepos extends JpaRepository<Annonces, Long> {
}
