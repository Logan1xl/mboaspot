package com.example.backend.services.implementations;

import com.example.backend.dto.AvisDTO;
import com.example.backend.entities.Avis;
import com.example.backend.mappers.AvisMapper;
import com.example.backend.repositories.AvisRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.interfaces.AvisInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvisService implements AvisInterface {

    private static final Logger logger = LoggerFactory.getLogger(AvisService.class);

    private AvisMapper avisMapper;
    private AvisRepos avisRepos;
    private VoyageurRepos voyageurRepos;

    public AvisService(AvisMapper avisMapper, AvisRepos avisRepos, VoyageurRepos voyageurRepos) {
        this.avisMapper = avisMapper;
        this.avisRepos = avisRepos;
        this.voyageurRepos = voyageurRepos;
    }

    @Override
    public AvisDTO ajouterAvis(AvisDTO avisDTO) {
        logger.info("Tentative d'ajout d'un avis");

        if (avisDTO.getPhotos() == null || avisDTO.getNote() == null
                || !voyageurRepos.existsById(avisDTO.getIdVoyageur().getId())) {

            logger.warn("Ajout avis refusé : photos/note/voyageur manquant");
            throw new IllegalArgumentException("Les photos,le voyageur et la note sont requises.");
        }

        try {
            Avis avis = avisMapper.toEntity(avisDTO);
            Avis savedAvis = avisRepos.save(avis);

            logger.info("Avis ajouté avec succès (id={})", savedAvis.getId());
            return avisMapper.toDTO(savedAvis);

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'avis", e);
            throw new RuntimeException("Erreur lors de l'ajout de l'avis: " + e.getMessage());
        }
    }

    @Override
    public List<AvisDTO> listerAvis() {
        logger.info("Récupération de la liste des avis");

        return avisRepos.findAll()
                .stream()
                .map(avisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AvisDTO mettreAJourAvis(Long id, AvisDTO avisDTO) {
        logger.info("Mise à jour de l'avis (id={})", id);

        try {
            Avis existingAvis = avisRepos.findById(avisDTO.getId()).orElse(null);

            if (existingAvis != null) {
                existingAvis.setNote(avisDTO.getNote());
                existingAvis.setPhotos(avisDTO.getPhotos());
                existingAvis.setEstSignale(avisDTO.getEstSignale());
                existingAvis.setRaisonSignalement(avisDTO.getRaisonSignalement());

                Avis updatedAvis = avisRepos.save(existingAvis);
                logger.info("Avis mis à jour avec succès (id={})", id);

                return avisMapper.toDTO(updatedAvis);
            } else {
                logger.warn("Avis non trouvé pour mise à jour (id={})", id);
                throw new RuntimeException("Avis non trouvé avec l'id: " + id);
            }

        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'avis (id={})", id, e);
            throw new RuntimeException("Erreur lors de la mise à jour de l'avis: " + e.getMessage());
        }
    }

    @Override
    public void supprimerAvis(Long id) {
        logger.info("Suppression de l'avis (id={})", id);

        if (avisRepos.existsById(id)) {
            try {
                avisRepos.deleteById(id);
                logger.info("Avis supprimé avec succès (id={})", id);

            } catch (Exception e) {
                logger.error("Erreur lors de la suppression de l'avis (id={})", id, e);
                throw new RuntimeException("Erreur lors de la suppression de l'avis: " + e.getMessage());
            }

        } else {
            logger.warn("Suppression impossible : avis non trouvé (id={})", id);
            throw new RuntimeException("Avis non trouvé avec l'id: " + id);
        }
    }

    @Override
    public AvisDTO obtenirAvisParId(Long id) {
        logger.info("Récupération de l'avis (id={})", id);

        if (avisRepos.existsById(id)) {
            try {
                Avis avis = avisRepos.findById(id).get();
                logger.debug("Avis récupéré : {}", avis);

                return avisMapper.toDTO(avis);

            } catch (Exception e) {
                logger.error("Erreur lors de la récupération de l'avis (id={})", id, e);
                throw new RuntimeException("Erreur lors de la récupération de l'avis: " + e.getMessage());
            }

        } else {
            logger.warn("Avis non trouvé (id={})", id);
            throw new RuntimeException("Avis non trouvé avec l'id: " + id);
        }
    }
}
