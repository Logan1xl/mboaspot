package com.example.backend.repositories;

import com.example.backend.entities.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {

    // ✅ OK car le champ EXISTE
    List<Equipement> findByNom(String nom);

    // ✅ OK car le champ EXISTE
    List<Equipement> findByIcone(String icone);
}

