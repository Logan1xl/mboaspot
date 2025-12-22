package com.example.backend.services;

import com.example.backend.dto.AnnonceDTO;
import com.example.backend.dto.DisponibiliteDTO;
import com.example.backend.dto.LocalisationDTO;
import com.example.backend.dto.RechercheDTO;
import com.example.backend.entities.*;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnoncesService {

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private LocalisationRepository localisationRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    // ===== CRUD Annonces =====

    public List<AnnonceDTO> getAllAnnonces() {
        return annoncesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceDTO> getAnnoncesActives() {
        return annoncesRepository.findAll()
                .stream()
                .filter(a -> a.getEstActive() != null && a.getEstActive())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AnnonceDTO> getAnnonceById(Long id) {
        return annoncesRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AnnonceDTO createAnnonce(AnnonceDTO dto) {
        Annonces annonce = convertToEntity(dto);
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

        Annonces saved = annoncesRepository.save(annonce);

        // Créer la localisation si fournie
        if (dto.getLocalisation() != null) {
            LocalisationDTO locDTO = dto.getLocalisation();
            Localisation localisation = new Localisation();
            localisation.setVille(locDTO.getVille());
            localisation.setQuartier(locDTO.getQuartier());
            localisation.setLatitude(locDTO.getLatitude());
            localisation.setLongitude(locDTO.getLongitude());
            localisation.setIdAnnonce(saved);
            localisationRepository.save(localisation);
        }

        return convertToDTO(saved);
    }

    public AnnonceDTO updateAnnonce(Long id, AnnonceDTO dto) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Mise à jour des champs
        annonce.setTitre(dto.getTitre());
        annonce.setPrix(dto.getPrix());
        annonce.setAdresse(dto.getAdresse());
        annonce.setLatitude(dto.getLatitude());
        annonce.setLongitude(dto.getLongitude());
        annonce.setVille(dto.getVille());
        annonce.setNbreChambres(dto.getNbreChambres());
        annonce.setNbreLits(dto.getNbreLits());
        annonce.setMaxInvites(dto.getMaxInvites());
        annonce.setDescription(dto.getDescription());
        annonce.setTypeAnnonce(dto.getTypeAnnonce());
        annonce.setUrlImagePrincipale(dto.getUrlImagePrincipale());

        if (dto.getUrlImages() != null) {
            annonce.setUrlImages(String.join(",", dto.getUrlImages()));
        }

        Annonces updated = annoncesRepository.save(annonce);
        return convertToDTO(updated);
    }

    public void deleteAnnonce(Long id) {
        annoncesRepository.deleteById(id);
    }

    public void activerAnnonce(Long id, Boolean activer) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée"));
        annonce.setEstActive(activer);
        annoncesRepository.save(annonce);
    }

    // ===== Recherche avancée =====

    public List<AnnonceDTO> rechercherAnnonces(RechercheDTO recherche) {
        List<Annonces> resultats;

        if (recherche.getLatitude() != null && recherche.getLongitude() != null && recherche.getRayon() != null) {
            // Recherche par géolocalisation
            resultats = annoncesRepository.findAnnoncesProches(
                    recherche.getLatitude(),
                    recherche.getLongitude(),
                    recherche.getRayon()
            );
        } else {
            // Recherche par critères
            resultats = annoncesRepository.rechercheAvancee(
                    recherche.getVille(),
                    recherche.getTypeAnnonce(),
                    recherche.getPrixMin(),
                    recherche.getPrixMax(),
                    recherche.getNbreChambres(),
                    recherche.getEvaluationMin()
            );
        }

        return resultats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceDTO> getTopAnnonces() {
        return annoncesRepository.findTop10ByEstActiveTrueOrderByEvaluationMoyenneDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ===== Gestion Disponibilité =====

    public List<DisponibiliteDTO> getDisponibilites(Long annonceId) {
        return disponibiliteRepository.findByIdAnnonceId(annonceId)
                .stream()
                .map(this::convertDisponibiliteToDTO)
                .collect(Collectors.toList());
    }

    public DisponibiliteDTO addDisponibilite(DisponibiliteDTO dto) {
        Annonces annonce = annoncesRepository.findById(dto.getIdAnnonce())
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        Disponibilite dispo = new Disponibilite();
        dispo.setEstDisponible(dto.getEstDisponible());
        dispo.setPrixSurcharge(dto.getPrixSurcharge());
        dispo.setDateDebut(dto.getDateDebut());
        dispo.setDateFin(dto.getDateFin());
        dispo.setIdAnnonce(annonce);

        Disponibilite saved = disponibiliteRepository.save(dispo);
        return convertDisponibiliteToDTO(saved);
    }

    public boolean verifierDisponibilite(Long annonceId, Date dateDebut, Date dateFin) {
        List<Disponibilite> dispos = disponibiliteRepository.findDisponibilitePourPeriode(
                annonceId, dateDebut, dateFin
        );
        return !dispos.isEmpty();
    }

    // ===== Gestion Localisation =====

    public Optional<LocalisationDTO> getLocalisation(Long annonceId) {
        return localisationRepository.findByIdAnnonceId(annonceId)
                .map(this::convertLocalisationToDTO);
    }

    public LocalisationDTO updateLocalisation(Long annonceId, LocalisationDTO dto) {
        Annonces annonce = annoncesRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        Localisation localisation = localisationRepository.findByIdAnnonceId(annonceId)
                .orElse(new Localisation());

        localisation.setVille(dto.getVille());
        localisation.setQuartier(dto.getQuartier());
        localisation.setLatitude(dto.getLatitude());
        localisation.setLongitude(dto.getLongitude());
        localisation.setIdAnnonce(annonce);

        Localisation saved = localisationRepository.save(localisation);
        return convertLocalisationToDTO(saved);
    }

    public List<String> getVillesDisponibles() {
        return localisationRepository.findAllVilles();
    }

    public List<String> getQuartiersByVille(String ville) {
        return localisationRepository.findQuartiersByVille(ville);
    }

    // ===== Conversions DTO <-> Entity =====

    private AnnonceDTO convertToDTO(Annonces annonce) {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setId(annonce.getId());
        dto.setTitre(annonce.getTitre());
        dto.setPrix(annonce.getPrix());
        dto.setAdresse(annonce.getAdresse());
        dto.setLatitude(annonce.getLatitude());
        dto.setLongitude(annonce.getLongitude());
        dto.setVille(annonce.getVille());
        dto.setNbreChambres(annonce.getNbreChambres());
        dto.setNbreLits(annonce.getNbreLits());
        dto.setMaxInvites(annonce.getMaxInvites());
        dto.setDescription(annonce.getDescription());
        dto.setEstActive(annonce.getEstActive());
        dto.setEvaluationMoyenne(annonce.getEvaluationMoyenne());
        dto.setTotalAvis(annonce.getTotalAvis());
        dto.setUrlImagePrincipale(annonce.getUrlImagePrincipale());
        dto.setTypeAnnonce(annonce.getTypeAnnonce());

        if (annonce.getUrlImages() != null) {
            dto.setUrlImages(Arrays.asList(annonce.getUrlImages().split(",")));
        }

        if (annonce.getIdProprietaire() != null) {
            dto.setIdProprietaire(annonce.getIdProprietaire().getId());
            if (annonce.getIdProprietaire().getIdUser() != null) {
                dto.setNomProprietaire(annonce.getIdProprietaire().getIdUser().getNom());
            }
        }

        return dto;
    }

    private Annonces convertToEntity(AnnonceDTO dto) {
        Annonces annonce = new Annonces();
        annonce.setTitre(dto.getTitre());
        annonce.setPrix(dto.getPrix());
        annonce.setAdresse(dto.getAdresse());
        annonce.setLatitude(dto.getLatitude());
        annonce.setLongitude(dto.getLongitude());
        annonce.setVille(dto.getVille());
        annonce.setNbreChambres(dto.getNbreChambres());
        annonce.setNbreLits(dto.getNbreLits());
        annonce.setMaxInvites(dto.getMaxInvites());
        annonce.setDescription(dto.getDescription());
        annonce.setTypeAnnonce(dto.getTypeAnnonce());
        annonce.setUrlImagePrincipale(dto.getUrlImagePrincipale());

        if (dto.getUrlImages() != null) {
            annonce.setUrlImages(String.join(",", dto.getUrlImages()));
        }

        if (dto.getIdProprietaire() != null) {
            Proprietaire proprio = proprietaireRepository.findById(dto.getIdProprietaire())
                    .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé"));
            annonce.setIdProprietaire(proprio);
        }

        return annonce;
    }

    private DisponibiliteDTO convertDisponibiliteToDTO(Disponibilite dispo) {
        DisponibiliteDTO dto = new DisponibiliteDTO();
        dto.setId(dispo.getId());
        dto.setEstDisponible(dispo.getEstDisponible());
        dto.setPrixSurcharge(dispo.getPrixSurcharge());
        dto.setDateDebut(dispo.getDateDebut());
        dto.setDateFin(dispo.getDateFin());
        dto.setIdAnnonce(dispo.getIdAnnonce().getId());
        return dto;
    }

    private LocalisationDTO convertLocalisationToDTO(Localisation loc) {
        LocalisationDTO dto = new LocalisationDTO();
        dto.setId(loc.getId());
        dto.setVille(loc.getVille());
        dto.setQuartier(loc.getQuartier());
        dto.setLatitude(loc.getLatitude());
        dto.setLongitude(loc.getLongitude());
        dto.setIdAnnonce(loc.getIdAnnonce().getId());
        return dto;
    }
}
