package com.example.backend.repositories;

import com.example.backend.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepos extends JpaRepository<Avis, Long> {
}
