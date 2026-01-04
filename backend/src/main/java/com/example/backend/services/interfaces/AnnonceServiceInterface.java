package com.example.backend.services.interfaces;

import com.example.backend.dto.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AnnonceServiceInterface {

    // Méthodes de la version simplifiée
    AnnonceResponseDTO creerAnnonce(AnnonceRequestDTO request);
    AnnonceResponseDTO obtenirAnnonce(Long id);
    List<AnnonceResponseDTO> obtenirAnnoncesActives();
    List<AnnonceResponseDTO> obtenirAnnoncesProprietaire(Long proprietaireId);
    AnnonceResponseDTO mettreAJourAnnonce(Long id, AnnonceRequestDTO request);
    AnnonceResponseDTO changerStatutAnnonce(Long id, Boolean estActive);
    void supprimerAnnonce(Long id);

    // Méthodes de la version complète
    List<AnnonceDTO> getAllAnnonces();
    List<AnnonceDTO> getAnnoncesActives();
    Optional<AnnonceDTO> getAnnonceById(Long id);
    AnnonceDTO createAnnonce(AnnonceDTO dto);
    AnnonceDTO updateAnnonce(Long id, AnnonceDTO dto);
    void deleteAnnonce(Long id);
    void activerAnnonce(Long id, Boolean activer);

    // Recherche avancée
    List<AnnonceDTO> rechercherAnnonces(RechercheDTO recherche);
    List<AnnonceDTO> getTopAnnonces();
    List<String> getVillesDisponibles();
    List<String> getQuartiersByVille(String ville);

    // Gestion disponibilité
    List<DisponibiliteDTO> getDisponibilites(Long annonceId);
    DisponibiliteDTO addDisponibilite(DisponibiliteDTO dto);
    boolean verifierDisponibilite(Long annonceId, Date dateDebut, Date dateFin);

    // Gestion localisation
    Optional<LocalisationDTO> getLocalisation(Long annonceId);
    LocalisationDTO updateLocalisation(Long annonceId, LocalisationDTO dto);
}