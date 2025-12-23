package com.example.backend.services.interfaces;

import com.example.backend.dto.UtilisateurDTO;

import java.util.List;

public interface UtilisateurInterface {
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateur);
    public List<UtilisateurDTO> getAllUtilisateurs();
    public UtilisateurDTO getUtilisateurById(Long id);
    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO utilisateurDTO);
    public void deleteUtilisateur(Long id);
}
