package com.example.backend.repositories;

import com.example.backend.entities.Voyageur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoyageurRepos extends JpaRepository<Voyageur, Long> {
}
