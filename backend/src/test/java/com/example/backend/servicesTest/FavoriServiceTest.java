package com.example.backend.servicesTest;

import com.example.backend.dto.FavoriDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Favori;
import com.example.backend.entities.Voyageur;
import com.example.backend.mappers.FavoriMapper;
import com.example.backend.repositories.AnnoncesRepos;
import com.example.backend.repositories.FavoriRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.implementation.FavoriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriServiceTest {

    @Mock
    private FavoriMapper favoriMapper;

    @Mock
    private FavoriRepos favoriRepos;

    @Mock
    private VoyageurRepos voyageurRepos;

    @Mock
    private AnnoncesRepos annoncesRepos;

    @InjectMocks
    private FavoriService favoriService;

    private FavoriDTO favoriDTO;
    private Favori favori;
    private Voyageur voyageur;
    private Annonces annonce;

    @BeforeEach
    void setUp() {
        voyageur = new Voyageur();
        voyageur.setId(1L);

        annonce = new Annonces();
        annonce.setId(1L);

        favoriDTO = new FavoriDTO();
        favoriDTO.setId(1L);
        favoriDTO.setIdVoyageur(voyageur);
        favoriDTO.setIdAnnonce(annonce);

        favori = new Favori();
        favori.setId(1L);
        favori.setIdVoyageur(voyageur);
        favori.setIdAnnonce(annonce);
    }

    @Test
    void ajouterFavori_ShouldReturnFavoriDTO_WhenDataIsValid() {
        when(annoncesRepos.existsById(1L)).thenReturn(true);
        when(voyageurRepos.existsById(1L)).thenReturn(true);
        when(favoriMapper.toEntity(favoriDTO)).thenReturn(favori);
        when(favoriRepos.save(favori)).thenReturn(favori);
        when(favoriMapper.toDTO(favori)).thenReturn(favoriDTO);

        FavoriDTO result = favoriService.ajouterFavori(favoriDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getIdVoyageur().getId());
        assertEquals(1L, result.getIdAnnonce().getId());
        verify(favoriRepos, times(1)).save(favori);
    }
/*
    @Test
    void ajouterFavori_ShouldThrowException_WhenAnnonceDoesNotExist() {
        when(annoncesRepos.existsById(1L)).thenReturn(false);
        when(voyageurRepos.existsById(1L)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> favoriService.ajouterFavori(favoriDTO)
        );

        assertEquals("L'ID de l'annonce et l'ID ddu voyageur sont requis et doivent exister.", exception.getMessage());
        verify(favoriRepos, never()).save(any());
    }*/

    @Test
    void ajouterFavori_ShouldThrowException_WhenVoyageurDoesNotExist() {
        when(annoncesRepos.existsById(1L)).thenReturn(true);
        when(voyageurRepos.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> favoriService.ajouterFavori(favoriDTO)
        );

        assertEquals("L'ID de l'annonce et l'ID du voyageur sont requis et doivent exister.", exception.getMessage());
        verify(favoriRepos, never()).save(any());
    }

    @Test
    void ajouterFavori_ShouldThrowRuntimeException_WhenSaveFails() {
        when(annoncesRepos.existsById(1L)).thenReturn(true);
        when(voyageurRepos.existsById(1L)).thenReturn(true);
        when(favoriMapper.toEntity(favoriDTO)).thenReturn(favori);
        when(favoriRepos.save(favori)).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> favoriService.ajouterFavori(favoriDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de l'ajout du favori"));
    }


    @Test
    void listerFavoris_ShouldReturnEmptyList_WhenNoFavoris() {
        when(favoriRepos.findAll()).thenReturn(Arrays.asList());

        List<FavoriDTO> result = favoriService.listerFavoris();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(favoriRepos, times(1)).findAll();
    }

    @Test
    void listerFavoris_ShouldReturnListOfFavoriDTO() {
        Favori favori2 = new Favori();
        favori2.setId(2L);

        FavoriDTO favoriDTO2 = new FavoriDTO();
        favoriDTO2.setId(2L);

        List<Favori> favorisList = Arrays.asList(favori, favori2);

        when(favoriRepos.findAll()).thenReturn(favorisList);
        when(favoriMapper.toDTO(favori)).thenReturn(favoriDTO);
        when(favoriMapper.toDTO(favori2)).thenReturn(favoriDTO2);

        List<FavoriDTO> result = favoriService.listerFavoris();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(favoriRepos, times(1)).findAll();
    }

    @Test
    void obtenirFavoriParId_ShouldReturnFavoriDTO_WhenIdExists() {
        when(favoriRepos.findById(1L)).thenReturn(Optional.of(favori));
        when(favoriMapper.toDTO(favori)).thenReturn(favoriDTO);

        FavoriDTO result = favoriService.obtenirFavoriParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(favoriRepos, times(1)).findById(1L);
    }

    @Test
    void obtenirFavoriParId_ShouldThrowException_WhenIdDoesNotExist() {
        when(favoriRepos.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> favoriService.obtenirFavoriParId(99L)
        );

        verify(favoriRepos, times(1)).findById(99L);
    }

    @Test
    void mettreAJourFavori_ShouldReturnUpdatedFavoriDTO_WhenIdExists() {
        Annonces nouvelleAnnonce = new Annonces();
        nouvelleAnnonce.setId(2L);
        favoriDTO.setIdAnnonce(nouvelleAnnonce);

        when(favoriRepos.findById(1L)).thenReturn(Optional.of(favori));
        when(favoriRepos.save(favori)).thenReturn(favori);
        when(favoriMapper.toDTO(favori)).thenReturn(favoriDTO);

        FavoriDTO result = favoriService.mettreAJourFavori(1L, favoriDTO);

        assertNotNull(result);
        assertEquals(2L, result.getIdAnnonce().getId());
        verify(favoriRepos, times(1)).save(favori);
    }

    @Test
    void mettreAJourFavori_ShouldThrowException_WhenIdDoesNotExist() {
        when(favoriRepos.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> favoriService.mettreAJourFavori(99L, favoriDTO)
        );

        verify(favoriRepos, never()).save(any());
    }

    @Test
    void mettreAJourFavori_ShouldThrowException_WhenUpdateFails() {
        when(favoriRepos.findById(1L)).thenReturn(Optional.of(favori));
        when(favoriRepos.save(favori)).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> favoriService.mettreAJourFavori(1L, favoriDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la mise à jour: " ));
    }

    @Test
    void supprimerFavori_ShouldDeleteFavori_WhenIdExists() {
        when(favoriRepos.existsById(1L)).thenReturn(true);
        doNothing().when(favoriRepos).deleteById(1L);

        assertDoesNotThrow(() -> favoriService.supprimerFavori(1L));

        verify(favoriRepos, times(1)).existsById(1L);
        verify(favoriRepos, times(1)).deleteById(1L);
    }

    @Test
    void supprimerFavori_ShouldThrowException_WhenIdDoesNotExist() {
        when(favoriRepos.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> favoriService.supprimerFavori(99L)
        );

        assertEquals("Favori non trouvé avec l'id: 99", exception.getMessage());
        verify(favoriRepos, never()).deleteById(any());
    }
}