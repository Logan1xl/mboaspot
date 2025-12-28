package com.example.backend.services;

import com.example.backend.dto.UtilisateurDTO;
import com.example.backend.entities.Utilisateur;
import com.example.backend.mappers.UtilisateurMapper;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.roles.RoleUtilisateur;
import com.example.backend.services.implementations.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private UtilisateurDTO utilisateurDTO;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(1L);
        utilisateurDTO.setEmail("test@example.com");
        utilisateurDTO.setMotDePasse("password123");
        utilisateurDTO.setPrenom("Jean");
        utilisateurDTO.setNom("Dupont");
        utilisateurDTO.setNumeroTelephone("+237612345678");
        utilisateurDTO.setPhotoProfil("photo.jpg");
        utilisateurDTO.setEstActif(true);
        utilisateurDTO.setRole(RoleUtilisateur.valueOf("VOYAGEUR"));

        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("test@example.com");
        utilisateur.setMotDePasse("encodedPassword");
        utilisateur.setPrenom("Jean");
        utilisateur.setNom("Dupont");
        utilisateur.setNumeroTelephone("+237612345678");
        utilisateur.setPhotoProfil("photo.jpg");
        utilisateur.setEstActif(true);
        utilisateur.setRole(RoleUtilisateur.valueOf("VOYAGEUR"));
    }


    @Test
    void getAllUtilisateurs_ShouldReturnEmptyList_WhenNoUtilisateurs() {
        when(repository.findAll()).thenReturn(Arrays.asList());

        List<UtilisateurDTO> result = utilisateurService.getAllUtilisateurs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getUtilisateurById_ShouldReturnUtilisateurDTO_WhenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(utilisateurDTO);

        UtilisateurDTO result = utilisateurService.getUtilisateurById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Jean", result.getPrenom());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getAllUtilisateurs_ShouldReturnListOfUtilisateurDTO() {
        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setId(2L);
        utilisateur2.setEmail("marie@example.com");

        UtilisateurDTO utilisateurDTO2 = new UtilisateurDTO();
        utilisateurDTO2.setId(2L);
        utilisateurDTO2.setEmail("marie@example.com");

        List<Utilisateur> utilisateurs = Arrays.asList(utilisateur, utilisateur2);

        when(repository.findAll()).thenReturn(utilisateurs);
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(utilisateurDTO);
        when(utilisateurMapper.toDTO(utilisateur2)).thenReturn(utilisateurDTO2);

        List<UtilisateurDTO> result = utilisateurService.getAllUtilisateurs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals("marie@example.com", result.get(1).getEmail());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getUtilisateurById_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.getUtilisateurById(99L)
        );

        assertEquals("Utilisateur non trouvé avec l'id: 99", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void createUtilisateur_ShouldReturnUtilisateurDTO_WhenDataIsValid() {
        when(repository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(utilisateurMapper.toEntity(utilisateurDTO)).thenReturn(utilisateur);
        when(repository.save(utilisateur)).thenReturn(utilisateur);
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(utilisateurDTO);

        UtilisateurDTO result = utilisateurService.createUtilisateur(utilisateurDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(repository, times(1)).save(utilisateur);
    }

    @Test
    void createUtilisateur_ShouldThrowException_WhenEmailAlreadyExists() {
        when(repository.existsByEmail("test@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.createUtilisateur(utilisateurDTO)
        );

        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void createUtilisateur_ShouldThrowException_WhenSaveFails() {
        when(repository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(utilisateurMapper.toEntity(utilisateurDTO)).thenReturn(utilisateur);
        when(repository.save(utilisateur)).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.createUtilisateur(utilisateurDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la création de l'utilisateur"));
    }

    @Test
    void updateUtilisateur_ShouldReturnUpdatedUtilisateurDTO_WhenIdExists() {
        utilisateurDTO.setPrenom("Pierre");
        utilisateurDTO.setNom("Martin");

        when(repository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(repository.save(utilisateur)).thenReturn(utilisateur);
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(utilisateurDTO);

        UtilisateurDTO result = utilisateurService.updateUtilisateur(1L, utilisateurDTO);

        assertNotNull(result);
        assertEquals("Pierre", result.getPrenom());
        assertEquals("Martin", result.getNom());
        verify(repository, times(1)).save(utilisateur);
    }

    @Test
    void updateUtilisateur_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.updateUtilisateur(99L, utilisateurDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la mise à jour de l'utilisateur"));
        verify(repository, never()).save(any());
    }

    @Test
    void updateUtilisateur_ShouldThrowException_WhenUpdateFails() {
        when(repository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(repository.save(utilisateur)).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.updateUtilisateur(1L, utilisateurDTO)
        );

        assertTrue(exception.getMessage().contains("Erreur lors de la mise à jour de l'utilisateur"));
    }

    @Test
    void updateUtilisateur_ShouldNotUpdatePassword() {
        String originalPassword = utilisateur.getMotDePasse();
        utilisateurDTO.setMotDePasse("newPassword");

        when(repository.findById(1L)).thenReturn(Optional.of(utilisateur));
        when(repository.save(utilisateur)).thenReturn(utilisateur);
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(utilisateurDTO);

        utilisateurService.updateUtilisateur(1L, utilisateurDTO);

        // Le mot de passe ne doit pas être modifié dans updateUtilisateur
        assertEquals(originalPassword, utilisateur.getMotDePasse());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deleteUtilisateur_ShouldDeleteUtilisateur_WhenIdExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> utilisateurService.deleteUtilisateur(1L));

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUtilisateur_ShouldThrowException_WhenIdDoesNotExist() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> utilisateurService.deleteUtilisateur(99L)
        );

        assertEquals("Utilisateur non trouvé avec l'id: 99", exception.getMessage());
        verify(repository, never()).deleteById(any());
    }
}