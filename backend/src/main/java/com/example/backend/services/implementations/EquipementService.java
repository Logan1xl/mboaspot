package com.example.backend.services.implementations;

import com.example.backend.dto.EquipementDTO;
import com.example.backend.entities.Equipement;
import com.example.backend.mappers.EquipementMapper;
import com.example.backend.repositories.EquipementRepository;
import com.example.backend.services.interfaces.EquipementInterface;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des équipements.
 * Contient la logique métier (services) entre le controller et la couche repository.
 */
@AllArgsConstructor
@Service
public class EquipementService implements EquipementInterface {

    private final EquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    // public EquipementService(EquipementRepository equipementRepository, EquipementMapper equipementMapper) {
    //     this.equipementRepository = equipementRepository;
    //     this.equipementMapper = equipementMapper;
    // }

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
     * @param id identifiant de l’équipement
     * @return Optional contenant le DTO si l'équipement est trouvé
     */
    @Override
    public EquipementDTO getById(Long id) {

        Equipement equipement = equipementRepository.findById(id).get();
        if (equipement==null){
            throw new RuntimeException("Equipement non trouvé");
        }else{
            return equipementMapper.toDto(equipement);
        }
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
     *
     * @param type type de l'équipement
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

   
    
    @Override
    public Optional<EquipementDTO> getEquipementById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEquipementById'");
    }

    @Override
    public List<EquipementDTO> getAllEquipements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllEquipements'");
    }

    @Override
    public EquipementDTO updateEquipement(Long id, EquipementDTO equipementDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEquipement'");
    }

    @Override
    public void deleteEquipement(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEquipement'");
    }

}

