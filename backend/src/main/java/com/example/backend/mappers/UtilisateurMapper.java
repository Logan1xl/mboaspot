package com.example.backend.mappers;

import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.entities.Utilisateur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

 /*   public UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        return new UtilisateurDTO(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getPrenom(),
                utilisateur.getNom(),
                utilisateur.getNumeroTelephone(),
                utilisateur.getPhotoProfil(),
                utilisateur.getEstActif(),
                utilisateur.getRole()
        );
    }

    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) {
            return null;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNom(dto.getNom());
        utilisateur.setNumeroTelephone(dto.getNumeroTelephone());
        utilisateur.setPhotoProfil(dto.getPhotoProfil());
        utilisateur.setEstActif(dto.getEstActif());
        utilisateur.setRole(dto.getRole());

        return utilisateur;
    }*/

    public Utilisateur toEntity(UtilisateurDTO utilisateurDTO);
    public UtilisateurDTO toDTO(Utilisateur utilisateur);

}