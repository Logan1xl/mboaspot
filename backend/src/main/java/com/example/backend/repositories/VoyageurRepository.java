package com.example.backend.repositories;

import com.example.backend.entities.Voyageur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoyageurRepository extends JpaRepository<Voyageur,Long> {
}
