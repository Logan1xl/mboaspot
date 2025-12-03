package com.example.backend.services.implementation;



import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.entities.Utilisateur;
import com.example.backend.mappers.UtilisateurMapper;
import com.example.backend.repositories.UtilisateurRepos;
import com.example.backend.services.interfaces.UtilisateurInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements UtilisateurInterface {


    private UtilisateurRepos repository;
    private PasswordEncoder passwordEncoder;
    private UtilisateurMapper utilisateurMapper;


    public UtilisateurService(UtilisateurRepos repository, PasswordEncoder passwordEncoder, UtilisateurMapper utilisateurMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.utilisateurMapper = utilisateurMapper;
    }

    public List<UtilisateurDTO> getAllUtilisateurs() {
        return repository.findAll()
                .stream()
                .map(utilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO getUtilisateurById(Long id) {
       try{
           Utilisateur utilisateur = repository.findById(id).get();
              return utilisateurMapper.toDTO(utilisateur);
       }catch(Exception e){
              throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
       }
    }

    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) {
        if (repository.existsByEmail(utilisateurDTO.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }else{
            try{
                utilisateurDTO.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
                return utilisateurMapper.toDTO(repository.save(utilisateurMapper.toEntity(utilisateurDTO)));
            }catch(Exception e){
                throw new RuntimeException("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            }

        }

    }

    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO utilisateurDTO) {
        try{
            Utilisateur existingUtilisateur = repository.findById(id).get();
            if (existingUtilisateur == null) {
                throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
            }else {
                existingUtilisateur.setNom(utilisateurDTO.getNom());
                existingUtilisateur.setPrenom(utilisateurDTO.getPrenom());
                existingUtilisateur.setEmail(utilisateurDTO.getEmail());
                existingUtilisateur.setRole(utilisateurDTO.getRole());
                repository.save(existingUtilisateur);
                return utilisateurMapper.toDTO(existingUtilisateur);
            }

        }catch (Exception e){
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
        }
    }

    public void deleteUtilisateur(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
        }
        repository.deleteById(id);
    }

}