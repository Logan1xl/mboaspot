package com.example.backend.services.implementations;

import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.entities.Utilisateur;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.mappers.UtilisateurMapper;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.services.interfaces.UtilisateurInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements UtilisateurInterface {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    private UtilisateurRepository repository;
    private PasswordEncoder passwordEncoder;
    private UtilisateurMapper utilisateurMapper;

    public UtilisateurService(UtilisateurRepository repository,
                              PasswordEncoder passwordEncoder,
                              UtilisateurMapper utilisateurMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Override
    public List<UtilisateurDTO> getAllUtilisateurs() {
        logger.info("Récupération de la liste des utilisateurs");

        return repository.findAll()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO getUtilisateurById(Long id) {
        logger.info("Récupération de l'utilisateur (id={})", id);

        Utilisateur utilisateur = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Utilisateur non trouvé (id={})", id);
                    return new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + id);
                });

        logger.debug("Utilisateur récupéré : {}", utilisateur);
        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) {
        logger.info("Création d'un nouvel utilisateur (email={})", utilisateurDTO.getEmail());

        if (repository.existsByEmail(utilisateurDTO.getEmail())) {
            logger.warn("Création utilisateur refusée : email déjà utilisé ({})", utilisateurDTO.getEmail());
            throw new RuntimeException("Email déjà utilisé");
        }

        try {
            utilisateurDTO.setMotDePasse(
                    passwordEncoder.encode(utilisateurDTO.getMotDePasse())
            );

            Utilisateur savedUtilisateur =
                    repository.save(utilisateurMapper.toEntity(utilisateurDTO));

            logger.info("Utilisateur créé avec succès (id={})", savedUtilisateur.getId());
            return utilisateurMapper.toDTO(savedUtilisateur);

        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur", e);
            throw new RuntimeException("Erreur lors de la création de l'utilisateur: " + e.getMessage());
        }
    }

    @Override
    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO utilisateurDTO) {
        logger.info("Mise à jour de l'utilisateur (id={})", id);

        Utilisateur existingUtilisateur = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Utilisateur non trouvé pour mise à jour (id={})", id);
                    return new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + id);
                });

        existingUtilisateur.setNom(utilisateurDTO.getNom());
        existingUtilisateur.setPrenom(utilisateurDTO.getPrenom());
        existingUtilisateur.setEmail(utilisateurDTO.getEmail());
        existingUtilisateur.setRole(utilisateurDTO.getRole());

        Utilisateur updatedUtilisateur = repository.save(existingUtilisateur);
        logger.info("Utilisateur mis à jour avec succès (id={})", id);

        return utilisateurMapper.toDTO(updatedUtilisateur);
    }

    @Override
    public void deleteUtilisateur(Long id) {
        logger.info("Suppression de l'utilisateur (id={})", id);

        if (!repository.existsById(id)) {
            logger.warn("Suppression impossible : utilisateur non trouvé (id={})", id);
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + id);
        }

        repository.deleteById(id);
        logger.info("Utilisateur supprimé avec succès (id={})", id);
    }
}
