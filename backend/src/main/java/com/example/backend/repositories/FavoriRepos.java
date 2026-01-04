package com.example.backend.repositories;


import com.example.backend.entities.Favori;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriRepos extends JpaRepository<Favori, Long> {
}
