package com.example.backend.services.interfaces;

import com.example.backend.dto.EquipementDTO;
import java.util.List;
import java.util.Optional;


public interface EquipementInterface {

    EquipementDTO save(EquipementDTO equipementDTO);

    Optional<EquipementDTO> getEquipementById(Long id);

    List<EquipementDTO> getAllEquipements();

    EquipementDTO updateEquipement(Long id, EquipementDTO equipementDTO);


    List<EquipementDTO> findByTypes(String nom);

    EquipementDTO update(Long id, EquipementDTO equipementDTO);

    List<EquipementDTO> getAll();


    EquipementDTO getById(Long id);

    void deleteById(Long id);


    void deleteEquipement(Long id);

    // Dans l'interface EquipementService
    void delete(Long id);







}
