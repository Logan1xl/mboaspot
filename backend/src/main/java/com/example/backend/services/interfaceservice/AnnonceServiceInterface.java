package com.example.backend.services.interfaceservice;

import com.example.backend.dto.reservationdto.AnnonceRequestDTO;
import com.example.backend.dto.reservationdto.AnnonceResponseDTO;

import java.util.List;

public interface AnnonceServiceInterface {

    // Créer une annonce
    AnnonceResponseDTO creerAnnonce(AnnonceRequestDTO request);

    // Obtenir une annonce par ID
    AnnonceResponseDTO obtenirAnnonce(Long id);

    // Obtenir toutes les annonces actives
    List<AnnonceResponseDTO> obtenirAnnoncesActives();

    // Obtenir les annonces d'un propriétaire
    List<AnnonceResponseDTO> obtenirAnnoncesProprietaire(Long proprietaireId);

    // Activer/Désactiver une annonce
    AnnonceResponseDTO changerStatutAnnonce(Long id, Boolean estActive);

    // Mettre à jour une annonce
    AnnonceResponseDTO mettreAJourAnnonce(Long id, AnnonceRequestDTO request);

    // Supprimer une annonce (désactivation)
    void supprimerAnnonce(Long id);
}