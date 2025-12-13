package com.example.backend.services.implementation;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Favori;
import com.example.backend.mappers.AnnoncesMapper;
import com.example.backend.mappers.FavoriMapper;
import com.example.backend.mappers.VoyageurMapper;
import com.example.backend.repositories.AnnoncesRepos;
import com.example.backend.repositories.FavoriRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.interfaces.FavoriInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriService implements FavoriInterface {
    private  FavoriMapper favoriMapper;
    private FavoriRepos favoriRepos;
    private VoyageurRepos voyageurRepos;
    private AnnoncesRepos annoncesRepos;


    public FavoriService(FavoriMapper favoriMapper, FavoriRepos favoriRepos, VoyageurRepos voyageurRepos, AnnoncesRepos annoncesRepos) {
        this.favoriMapper = favoriMapper;
        this.favoriRepos = favoriRepos;
        this.voyageurRepos = voyageurRepos;
        this.annoncesRepos = annoncesRepos;

    }

    @Override
    public FavoriDTO ajouterFavori(FavoriDTO favoriDTO) {
        if ( !annoncesRepos.existsById(favoriDTO.getIdAnnonce().getId()) || voyageurRepos.existsById(favoriDTO.getIdVoyageur().getId())) {
            throw new IllegalArgumentException("L'ID de l'annonce et l'ID ddu voyageur sont requis et doivent exister.");
        }else {
            try {
                return favoriMapper.toDTO(favoriRepos.save(favoriMapper.toEntity(favoriDTO)));
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de l'ajout du favori: " + e.getMessage());
            }
        }
    }

    @Override
    public List<FavoriDTO> listerFavoris() {
        return favoriRepos.findAll()
                .stream()
                .map(favoriMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FavoriDTO mettreAJourFavori(Long id, FavoriDTO favoriDTO) {
        Favori favori1 = favoriRepos.findById(id).get();
        if (favori1 != null) {
            try{
                favori1.setIdAnnonce(favoriDTO.getIdAnnonce());
                favori1.setIdVoyageur(favoriDTO.getIdVoyageur());
                return favoriMapper.toDTO(favoriRepos.save(favori1));
            } catch (Exception e) {
                throw new RuntimeException("erreur lors de la mise a jour" +e.getMessage());
            }

        }else {
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public void supprimerFavori(Long id) {
        if (favoriRepos.existsById(id)){
            favoriRepos.deleteById(id);
        }else {
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }

    @Override
    public FavoriDTO obtenirFavoriParId(Long id) {
        Favori favori1 = favoriRepos.findById(id).get();
        if (favori1 != null) {
            try{
                return favoriMapper.toDTO(favori1);
            } catch (Exception e) {
                throw new RuntimeException("erreur " +e.getMessage());
            }

        }else {
            throw new RuntimeException("Favori non trouvé avec l'id: " + id);
        }
    }
}