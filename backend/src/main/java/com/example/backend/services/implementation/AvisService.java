package com.example.backend.services.implementation;

import com.example.backend.dto.AvisDTO;
import com.example.backend.entities.Avis;
import com.example.backend.mappers.AvisMapper;
import com.example.backend.repositories.AvisRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.interfaces.AvisInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvisService implements AvisInterface {
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
        if (avisDTO.getPhotos() == null || avisDTO.getNote() == null || !voyageurRepos.existsById(avisDTO.getIdVoyageur().getId())){
            throw new IllegalArgumentException("Les photos,le voyageur et la note sont requises.");
        }else {
            try{
                return avisMapper.toDTO(avisRepos.save(avisMapper.toEntity(avisDTO)));
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de l'ajout de l'avis: " + e.getMessage());
            }
        }
    }

    @Override
    public List<AvisDTO> listerAvis() {
        return avisRepos.findAll()
                .stream()
                .map(avisMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AvisDTO mettreAJourAvis(Long id, AvisDTO avisDTO) {
        try{
            Avis existingAvis = avisRepos.findById(avisDTO.getId()).get();
            if (existingAvis != null) {
                existingAvis.setNote(avisDTO.getNote());
                existingAvis.setPhotos(avisDTO.getPhotos());
                existingAvis.setEstSignale(avisDTO.getEstSignale());
                existingAvis.setRaisonSignalement(avisDTO.getRaisonSignalement());
                return avisMapper.toDTO(avisRepos.save(existingAvis));
            } else {
                throw new RuntimeException("Avis non trouvé avec l'id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'avis: " + e.getMessage());
        }
    }

    @Override
    public void supprimerAvis(Long id) {
        if (avisRepos.existsById(id)){
            try{
                avisRepos.deleteById(id);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la suppression de l'avis: " + e.getMessage());
            }

        }else {
            throw new RuntimeException("Avis non trouvé avec l'id: " + id);
        }
    }

    @Override
    public AvisDTO obtenirAvisParId(Long id) {
        if (avisRepos.existsById(id)){
            try{
                Avis avis = avisRepos.findById(id).get();
                return avisMapper.toDTO(avis);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la récupération de l'avis: " + e.getMessage());

            }
        }else {
            throw new RuntimeException("Avis non trouvé avec l'id: " + id);
        }
    }
}
