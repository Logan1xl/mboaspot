package com.example.backend.services.interfaces;

import com.example.backend.dto.FavoriDTO;

import java.util.List;

public interface FavoriInterface {
    FavoriDTO ajouterFavori(FavoriDTO favoriDTO);
    List<FavoriDTO> listerFavoris();
    FavoriDTO mettreAJourFavori(Long id, FavoriDTO favoriDTO);
    void supprimerFavori(Long id);
    FavoriDTO obtenirFavoriParId(Long id);
}
