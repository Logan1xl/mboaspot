package com.example.backend.services.implementation;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Favori;
import com.example.backend.mappers.FavoriMapper;
import com.example.backend.repositories.AnnoncesRepos;
import com.example.backend.repositories.FavoriRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.interfaces.FavoriInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriService implements FavoriInterface {

    private static final Logger logger = LoggerFactory.getLogger(FavoriService.class);

    private FavoriMapper favoriMapper;
    private FavoriRepos favoriRepos;
    private VoyageurRepos voyageurRepos;
    private AnnoncesRepos annoncesRepos;

    public FavoriService(FavoriMapper favoriMapper,
                         FavoriRepos favoriRepos,
                         VoyageurRepos voyageurRepos,
                         AnnoncesRepos annoncesRepos) {
        this.favoriMapper = favoriMapper;
        this.favoriRepos = favoriRepos;
        this.voyageurRepos = voyageurRepos;
        this.annoncesRepos = annoncesRepos;
    }

    @Override
    public FavoriDTO ajouterFavori(FavoriDTO favoriDTO) {
        logger.info("Tentative d'ajout d'un favori");

        if (!annoncesRepos.existsById(favoriDTO.getIdAnnonce().getId())
                || !voyageurRepos.existsById(favoriDTO.getIdVoyageur().getId())) {

            logger.warn("Ajout favori refusé : annonce ou voyageur inexistant");
            throw new IllegalArgumentException(
                    "L'ID de l'annonce et l'ID du voyageur sont requis et doivent exister.");
        }

        try {
            Favori favori = favoriMapper.toEntity(favoriDTO);
            Favori savedFavori = favoriRepos.save(favori);

            logger.info("Favori ajouté avec succès (id={})", savedFavori.getId());
            return favoriMapper.toDTO(savedFavori);

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du favori", e);
            throw new RuntimeException("Erreur lors de l'ajout du favori: " + e.getMessage());
        }
    }

    @Override
    public List<FavoriDTO> listerFavoris() {
        logger.info("Récupération de la liste des favoris");

        return favoriRepos.findAll()
                .stream()
                .map(favoriMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriDTO mettreAJourFavori(Long id, FavoriDTO favoriDTO) {
        logger.info("Mise à jour du favori (id={})", id);

        Favori favori1 = favoriRepos.findById(id).orElse(null);

        if (favori1 != null) {
            try {
                favori1.setIdAnnonce(favoriDTO.getIdAnnonce());
                favori1.setIdVoyageur(favoriDTO.getIdVoyageur());

                Favori updatedFavori = favoriRepos.save(favori1);
                logger.info("Favori mis à jour avec succès (id={})", id);

                return favoriMapper.toDTO(updatedFavori);

            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour du favori (id={})", id, e);
                throw new RuntimeException("Erreur lors de la mise à jour: " + e.getMessage());
            }

        } else {
            logger.warn("Favori non trouvé pour mise à jour (id={})", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public void supprimerFavori(Long id) {
        logger.info("Suppression du favori (id={})", id);

        if (favoriRepos.existsById(id)) {
            favoriRepos.deleteById(id);
            logger.info("Favori supprimé avec succès (id={})", id);
        } else {
            logger.warn("Suppression impossible : favori non trouvé (id={})", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public FavoriDTO obtenirFavoriParId(Long id) {
        logger.info("Récupération du favori (id={})", id);

        Favori favori1 = favoriRepos.findById(id).orElse(null);

        if (favori1 != null) {
            try {
                logger.debug("Favori récupéré : {}", favori1);
                return favoriMapper.toDTO(favori1);

            } catch (Exception e) {
                logger.error("Erreur lors de la récupération du favori (id={})", id, e);
                throw new RuntimeException("Erreur: " + e.getMessage());
            }

        } else {
            logger.warn("Favori non trouvé (id={})", id);
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }
}
