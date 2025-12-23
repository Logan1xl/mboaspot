package com.example.backend.services;

import com.example.backend.dto.AvisDTO;
import com.example.backend.entities.Avis;
import com.example.backend.entities.Voyageur;
import com.example.backend.mappers.AvisMapper;
import com.example.backend.repositories.AvisRepos;
import com.example.backend.repositories.VoyageurRepos;
import com.example.backend.services.implementation.AvisService;
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
class AvisServiceTest {

    @Mock
    private AvisMapper avisMapper;

    @Mock
    private AvisRepos avisRepos;

    @Mock
    private VoyageurRepos voyageurRepos;

    @InjectMocks
    private AvisService avisService;

    private AvisDTO avisDTO;
    private Avis avis;
    private Voyageur voyageur;

    @BeforeEach
    void setUp() {
        voyageur = new Voyageur();
        voyageur.setId(1L);

        avisDTO = new AvisDTO();
        avisDTO.setId(1L);
        avisDTO.setNote(5);
        avisDTO.setPhotos("photo1.jpg");
        avisDTO.setEstSignale(false);
        avisDTO.setRaisonSignalement(null);
        avisDTO.setIdVoyageur(voyageur);

        avis = new Avis();
        avis.setId(1L);
        avis.setNote(5);
        avis.setPhotos("photo1.jpg");
        avis.setEstSignale(false);
    }

    @Test
    void ajouterAvis_ShouldReturnAvisDTO_WhenDataIsValid() {
        when(voyageurRepos.existsById(1L)).thenReturn(true);
        when(avisMapper.toEntity(avisDTO)).thenReturn(avis);
        when(avisRepos.save(avis)).thenReturn(avis);
        when(avisMapper.toDTO(avis)).thenReturn(avisDTO);

        AvisDTO result = avisService.ajouterAvis(avisDTO);

        assertNotNull(result);
        assertEquals(5, result.getNote());
        assertEquals("photo1.jpg", result.getPhotos());
        verify(avisRepos, times(1)).save(avis);
        verify(voyageurRepos, times(1)).existsById(1L);
    }



    @Test
    void ajouterAvis_ShouldThrowException_WhenNoteIsNull() {
        avisDTO.setNote(null);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avisService.ajouterAvis(avisDTO)
        );

        assertEquals("Les photos,le voyageur et la note sont requises.", exception.getMessage());
        verify(avisRepos, never()).save(any());
    }

    @Test
    void ajouterAvis_ShouldThrowException_WhenPhotosIsNull() {
        avisDTO.setPhotos(null);


        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avisService.ajouterAvis(avisDTO)
        );

        assertEquals("Les photos,le voyageur et la note sont requises.", exception.getMessage());
        verify(avisRepos, never()).save(any());
    }

    @Test
    void ajouterAvis_ShouldThrowException_WhenVoyageurDoesNotExist() {
        when(voyageurRepos.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> avisService.ajouterAvis(avisDTO)
        );

        assertEquals("Les photos,le voyageur et la note sont requises.", exception.getMessage());
        verify(avisRepos, never()).save(any());
    }

    @Test
    void ajouterAvis_ShouldThrowRuntimeException_WhenSaveFails() {
        when(voyageurRepos.existsById(1L)).thenReturn(true);
        when(avisMapper.toEntity(avisDTO)).thenReturn(avis);
        when(avisRepos.save(avis)).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> avisService.ajouterAvis(avisDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de l'ajout de l'avis"));
    }

    @Test
    void listerAvis_ShouldReturnListOfAvisDTO() {
        Avis avis2 = new Avis();
        avis2.setId(2L);
        avis2.setNote(4);

        AvisDTO avisDTO2 = new AvisDTO();
        avisDTO2.setId(2L);
        avisDTO2.setNote(4);

        List<Avis> avisList = Arrays.asList(avis, avis2);

        when(avisRepos.findAll()).thenReturn(avisList);
        when(avisMapper.toDTO(avis)).thenReturn(avisDTO);
        when(avisMapper.toDTO(avis2)).thenReturn(avisDTO2);

        List<AvisDTO> result = avisService.listerAvis();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getNote());
        assertEquals(4, result.get(1).getNote());
        verify(avisRepos, times(1)).findAll();
    }

    @Test
    void listerAvis_ShouldReturnEmptyList_WhenNoAvis() {
        when(avisRepos.findAll()).thenReturn(Arrays.asList());

        List<AvisDTO> result = avisService.listerAvis();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(avisRepos, times(1)).findAll();
    }

    @Test
    void obtenirAvisParId_ShouldReturnAvisDTO_WhenIdExists() {
        when(avisRepos.existsById(1L)).thenReturn(true);
        when(avisRepos.findById(1L)).thenReturn(Optional.of(avis));
        when(avisMapper.toDTO(avis)).thenReturn(avisDTO);

        AvisDTO result = avisService.obtenirAvisParId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getNote());
        verify(avisRepos, times(1)).findById(1L);
    }

    @Test
    void obtenirAvisParId_ShouldThrowException_WhenIdDoesNotExist() {
        when(avisRepos.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> avisService.obtenirAvisParId(99L)
        );

        assertEquals("Avis non trouvé avec l'id: 99", exception.getMessage());
        verify(avisRepos, never()).findById(any());
    }

    @Test
    void mettreAJourAvis_ShouldReturnUpdatedAvisDTO_WhenIdExists() {
        avisDTO.setNote(3);
        avisDTO.setEstSignale(true);
        avisDTO.setRaisonSignalement("Contenu inapproprié");

        when(avisRepos.findById(1L)).thenReturn(Optional.of(avis));
        when(avisRepos.save(avis)).thenReturn(avis);
        when(avisMapper.toDTO(avis)).thenReturn(avisDTO);

        AvisDTO result = avisService.mettreAJourAvis(1L, avisDTO);

        assertNotNull(result);
        assertEquals(3, result.getNote());
        assertEquals(true, result.getEstSignale());
        assertEquals("Contenu inapproprié", result.getRaisonSignalement());
        verify(avisRepos, times(1)).save(avis);
    }

    @Test
    void mettreAJourAvis_ShouldThrowException_WhenIdDoesNotExist() {
        when(avisRepos.findById(99L)).thenReturn(Optional.empty());
        avisDTO.setId(99L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> avisService.mettreAJourAvis(99L, avisDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la mise à jour de l'avis"));
        verify(avisRepos, never()).save(any());
    }

    @Test
    void supprimerAvis_ShouldDeleteAvis_WhenIdExists() {
        when(avisRepos.existsById(1L)).thenReturn(true);
        doNothing().when(avisRepos).deleteById(1L);

        assertDoesNotThrow(() -> avisService.supprimerAvis(1L));

        verify(avisRepos, times(1)).existsById(1L);
        verify(avisRepos, times(1)).deleteById(1L);
    }

    @Test
    void supprimerAvis_ShouldThrowException_WhenIdDoesNotExist() {
        when(avisRepos.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> avisService.supprimerAvis(99L)
        );

        assertEquals("Avis non trouvé avec l'id: 99", exception.getMessage());
        verify(avisRepos, never()).deleteById(any());
    }

    @Test
    void supprimerAvis_ShouldThrowException_WhenDeleteFails() {
        when(avisRepos.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Erreur DB")).when(avisRepos).deleteById(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> avisService.supprimerAvis(1L)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la suppression de l'avis"));
    }
}