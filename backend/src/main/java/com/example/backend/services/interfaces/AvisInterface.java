package com.example.backend.services.interfaces;

import com.example.backend.dto.AvisDTO;

import java.util.List;

public interface AvisInterface {
    AvisDTO ajouterAvis(AvisDTO avisDTO);
    List<AvisDTO> listerAvis();
    AvisDTO mettreAJourAvis(Long id, AvisDTO avisDTO);
    void supprimerAvis(Long id);
    AvisDTO obtenirAvisParId(Long id);
}
