package com.example.backend.services.implementations;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.entities.Equipement;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.mappers.EquipementMapper;
import com.example.backend.repositories.EquipementRepository;
import com.example.backend.services.interfaces.EquipementInterface;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des équipements.
 * Contient la logique métier (services) entre le controller et la couche repository.
 */
@AllArgsConstructor
@Service
public class EquipementService implements EquipementInterface {

    private static final Logger logger = LoggerFactory.getLogger(EquipementService.class);

    private final EquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;



    /**
     * Enregistre un nouvel équipement en base.
     *
     * @param equipementDTO DTO contenant les données de l'équipement
     * @return l'équipement enregistré sous forme de DTO
     */
    @Override
    public EquipementDTO save(EquipementDTO equipementDTO) {
       Equipement equipement = equipementMapper.toEntity(equipementDTO);
        Equipement saved = equipementRepository.save(equipement);
        return equipementMapper.toDto(saved);
    }

    /**
     * Récupère tous les équipements enregistrés.
     *
     * @return liste des équipements en DTO
     */
    @Override
    public List<EquipementDTO> getAll() {
        return equipementRepository.findAll()
               .stream()
                .map(equipementMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Récupère un équipement par son ID.
     *
     * @param id identifiant de l'équipement
     * @return DTO de l'équipement trouvé
     * @throws ResourceNotFoundException si l'équipement n'existe pas
     */
    @Override
    public EquipementDTO getById(Long id) {
        logger.debug("Récupération de l'équipement (id={})", id);
        
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement non trouvé avec l'id : " + id));
        
        return equipementMapper.toDto(equipement);
    }

    /**
     * Supprime un équipement via son ID.
     *
     * @param id identifiant de l’équipement
     */
    @Override
    public void deleteById(Long id) {
        equipementRepository.deleteById(id);
    }

    /**
     * Recherche les équipements par type.
     * @return liste des équipements correspondants
     */
    @Override
    public List<EquipementDTO> findByTypes(String nom) {
        List<Equipement> equipements = equipementRepository.findByNom(nom);
        return equipementMapper.toDto(equipements);
    }

    /**
     * Met à jour un équipement existant.
     *
     * @param id identifiant de l'équipement à mettre à jour
     * @param equipementDTO nouvelles données
     * @return DTO de l'équipement mis à jour
     */
    @Override
    public EquipementDTO update(Long id, EquipementDTO equipementDTO) {
        // Reconstruction de l'entité à partir du DTO
        Equipement equipement = equipementMapper.toEntity(equipementDTO);

        // Mise à jour de l'ID pour indiquer qu'on modifie un existant
        equipement.setId(id);

        // Enregistrement en base
        Equipement updatedEquipement = equipementRepository.save(equipement);

        // Retour en DTO
        return equipementMapper.toDto(updatedEquipement);
    }

    /**
     * Récupère un équipement par son ID (méthode avec Optional).
     *
     * @param id identifiant de l'équipement
     * @return Optional contenant le DTO si l'équipement est trouvé
     */
    @Override
    public Optional<EquipementDTO> getEquipementById(Long id) {
        logger.debug("Récupération de l'équipement (id={})", id);
        
        return equipementRepository.findById(id)
                .map(equipementMapper::toDto);
    }

    /**
     * Récupère tous les équipements (alias de getAll).
     *
     * @return liste de tous les équipements en DTO
     */
    @Override
    public List<EquipementDTO> getAllEquipements() {
        logger.debug("Récupération de tous les équipements");
        return getAll();
    }

    /**
     * Met à jour un équipement existant (alias de update).
     *
     * @param id identifiant de l'équipement à mettre à jour
     * @param equipementDTO nouvelles données
     * @return DTO de l'équipement mis à jour
     */
    @Override
    public EquipementDTO updateEquipement(Long id, EquipementDTO equipementDTO) {
        logger.debug("Mise à jour de l'équipement (id={})", id);
        return update(id, equipementDTO);
    }

    /**
     * Supprime un équipement (alias de delete).
     *
     * @param id identifiant de l'équipement à supprimer
     */
    @Override
    public void deleteEquipement(Long id) {
        logger.debug("Suppression de l'équipement (id={})", id);
        delete(id);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Suppression de l'équipement (id={})", id);
        
        // Vérifie si l'équipement existe avant de supprimer
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Équipement non trouvé avec l'id : " + id);
        }
        
        equipementRepository.deleteById(id);
        logger.info("Équipement supprimé avec succès (id={})", id);
    }

}

