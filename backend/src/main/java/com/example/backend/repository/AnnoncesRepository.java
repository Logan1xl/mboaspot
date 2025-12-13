package com.example.backend.repository;

import com.example.backend.entities.Annonces;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnoncesRepository extends JpaRepository<Annonces,Long> {
    Annonces findAnnoncesById(Long id);
}
