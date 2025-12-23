package com.example.backend.dto;

import com.example.backend.entities.Equipement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipementDTO {

    private Long id;
  
    private String nom;
  
    private String icone;
   
    private String description;
  


}
