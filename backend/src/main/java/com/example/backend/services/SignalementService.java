package com.example.backend.services;

import com.example.backend.dto.SignalementRequestDTO;
import com.example.backend.entities.Admin;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Signalement;
import com.example.backend.entities.Utilisateur;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.repositories.AdminRepository;
import com.example.backend.repositories.AnnoncesRepository;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.repositories.SignalementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignalementService {

    private final SignalementRepository signalementRepository;
    private final AnnoncesRepository annoncesRepository;
    private final AdminRepository adminRepository;


    public SignalementService(
            SignalementRepository signalementRepository,
            AnnoncesRepository annoncesRepository,AdminRepository adminRepository) {
        this.signalementRepository = signalementRepository;
        this.annoncesRepository = annoncesRepository;
        this.adminRepository=adminRepository;

    }
    public Signalement creer(SignalementRequestDTO dto) {

        Annonces annonce = annoncesRepository.findById(dto.getAnnonceId())
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        Admin admin = adminRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));


        Signalement s = new Signalement();
        s.setRaison(dto.getRaison());
        s.setDescription(dto.getDescription());
        s.setStatut("EN_ATTENTE");
        s.setIdAdmin(admin);
        s.setIdAnnonce(annonce);

        return signalementRepository.save(s);
    }

    // READ ALL
    public List<Signalement> getAll() {
        return signalementRepository.findAll();
    }

    // READ ONE
    public Signalement getById(Long id) {
        return signalementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Signalement non trouvé"));
    }

    // UPDATE
    public Signalement traiter(Long id, String statut, String resolution) {
        Signalement s = getById(id);
        s.setStatut(statut);
        s.setResolution(resolution);
        return signalementRepository.save(s);
    }

    // DELETE
    public void supprimer(Long id) {
        signalementRepository.deleteById(id);
    }
}
