package com.example.backend.services.interfaces;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.entities.Localisation;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service de gestion des localisations.
 * Définit les opérations CRUD disponibles.
 */
public interface LocalisationInterface {

   /**
    * Crée une nouvelle localisation.
    *
    * @param localisationDTO les données de la localisation à créer
    * @return LocalisationDTO la localisation créée
    */
    LocalisationDTO save(LocalisationDTO localisationDTO);

    /**
     * Récupère une localisation par son ID.
     *
     * @param id identifiant de la localisation
     * @return Optional<LocalisationDTO> la localisation trouvée ou vide si non trouvée
     */
    LocalisationDTO getById(Long id);

    /**
     * Récupère toutes les localisations.
     *
     * @return liste de LocalisationDTO
     */
    List<LocalisationDTO> getAll();

    /**
     * Met à jour une localisation existante.
     *
     * @param id identifiant de la localisation à mettre à jour
     * @param localisationDTO les nouvelles données de la localisation
     * @return LocalisationDTO la localisation mise à jour
     */
    LocalisationDTO update(Long id, LocalisationDTO localisationDTO);

    /**
     * Supprime une localisation par son ID.
     *
     * @param id identifiant de la localisation à supprimer
     */
    void delete(Long id);

}
