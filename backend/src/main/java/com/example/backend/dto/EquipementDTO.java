package com.example.backend.dto;

public class EquipementDTO {
    private Long id;
    private String nom;
    private String icone;
    private String description;

    // Constructeurs
    public EquipementDTO() {}

    public EquipementDTO(Long id, String nom, String icone, String description) {
        this.id = id;
        this.nom = nom;
        this.icone = icone;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
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
