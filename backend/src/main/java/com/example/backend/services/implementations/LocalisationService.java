package com.example.backend.services.implementations;

import com.example.backend.dto.LocalisationDTO;
import com.example.backend.entities.Localisation;
import com.example.backend.mappers.LocalisationMapper;
import com.example.backend.services.interfaces.LocalisationInterface;
import org.springframework.stereotype.Service;
import com.example.backend.repositories.LocalisationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service permettant de gérer les opérations CRUD
 * sur l'entité Localisation.
 * Implémente LocalisationService.
 */
@Service
public class LocalisationService implements LocalisationInterface {

    /**
     * Repository pour accéder à la base de données.
     */
    
    private LocalisationRepository localisationRepository;

    /**
     * Mapper MapStruct pour convertir Entity <-> DTO.
     */
    
    private LocalisationMapper localisationMapper;

    public LocalisationService(LocalisationRepository localisationRepository, LocalisationMapper localisationMapper) {
        this.localisationRepository = localisationRepository;
        this.localisationMapper = localisationMapper;
    }

    /**
     * Enregistre une nouvelle localisation.
     *
     * @param localisationDTO DTO contenant les données.
     * @return DTO de la localisation sauvegardée.
     */
    @Override
    public LocalisationDTO save(LocalisationDTO localisationDTO) {
        Localisation localisation = localisationMapper.toEntity(localisationDTO);
        Localisation saved = localisationRepository.save(localisation);
        return localisationMapper.toDto(saved);
    }

    /**
     * Retourne la liste de toutes les localisations.
     */
    @Override
    public List<LocalisationDTO> getAll() {
        return localisationRepository.findAll().stream().map(
        localisationMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Recherche une localisation par son id.
     */
    @Override
    public LocalisationDTO getById(Long id) {
        Localisation localisation = localisationRepository.findById(id).get();
        if (localisation == null){
            throw new RuntimeException("Localisation non trouvée");
        }else {
        return localisationMapper.toDto(localisation);
    }
    }

    /**
     * Supprime une localisation selon son id.
     */
        @Override
        public void delete(Long id) {
            localisationRepository.deleteById(id);

        }

    /**
     * Met à jour une localisation existante.
     *
     * @param id identifiant de la localisation à modifier
     * @param localisationDTO nouvelle valeur
     * @return localisation mise à jour
     */
    @Override
    public LocalisationDTO update(Long id, LocalisationDTO localisationDTO) {
        // Convertir le DTO en entité
        Localisation localisation = localisationMapper.toEntity(localisationDTO);

        // Assigner l’ID existant
        localisation.setId(id);

        // Sauvegarder les modifications
        Localisation updated = localisationRepository.save(localisation);

        // Retourner le résultat au format DTO
        return localisationMapper.toDto(updated);
    }


    }




