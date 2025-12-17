package com.example.backend.repositories;

import com.example.backend.entities.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignalementRepository extends JpaRepository<Signalement, Long> {


    List<Signalement> findByStatut(String statut);

    List<Signalement> findByIdAnnonce_Id(Long annonceId);

    List<Signalement> findByIdAdmin_Id(Long adminId);
}
