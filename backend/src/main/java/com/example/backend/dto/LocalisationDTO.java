package com.example.backend.dto;

import com.example.backend.entities.Annonces;
import org.hibernate.validator.constraints.Length;

import com.example.backend.entities.Localisation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalisationDTO {
    private Long id;
   
    private String ville;

   
    private String quartier;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Length(max = 50)
    private Double latitude;
   @Length(max = 50)
    private Double longitude;
   
    private Annonces idAnnonce;


}
