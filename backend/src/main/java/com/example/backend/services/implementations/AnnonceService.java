package com.example.backend.services.implementations;

import com.example.backend.dto.*;
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
public class AnnonceService {

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Autowired
    private LocalisationRepository localisationRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    // ===== CRUD Annonces =====

    /**
     * Créer une annonce avec disponibilité par défaut
     */
    public AnnonceResponseDTO creerAnnonce(AnnonceRequestDTO request) {
        // Vérifier que le propriétaire existe
        Proprietaire proprietaire = proprietaireRepository.findById(request.getProprietaireId())
                .orElseThrow(() -> new RuntimeException("Propriétaire non trouvé"));

        // Créer l'annonce
        Annonces annonce = new Annonces();
        annonce.setTitre(request.getTitre());
        annonce.setPrix(request.getPrix());
        annonce.setAdresse(request.getAdresse());
        annonce.setVille(request.getVille());
        annonce.setNbreChambres(request.getNbreChambres());
        annonce.setNbreLits(request.getNbreLits());
        annonce.setMaxInvites(request.getMaxInvites());
        annonce.setDescription(request.getDescription());
        annonce.setTypeAnnonce(request.getTypeAnnonce());
        annonce.setIdProprietaire(proprietaire);
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

        if (request.getLatitude() != null) {
            annonce.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            annonce.setLongitude(request.getLongitude());
        }

        annonce = annoncesRepository.save(annonce);

        // Créer la localisation si ville et quartier sont fournis
        if (request.getVille() != null) {
            Localisation localisation = new Localisation();
            localisation.setVille(request.getVille());
            localisation.setQuartier(request.getQuartier() != null ? request.getQuartier() : "Centre");
            localisation.setLatitude(request.getLatitude());
            localisation.setLongitude(request.getLongitude());
            localisation.setIdAnnonce(annonce);
            localisationRepository.save(localisation);
        }

        creerDisponibiliteParDefaut(annonce);

        return convertirEnResponseDTO(annonce);
    }

    public AnnonceDTO createAnnonce(AnnonceDTO dto) {
        Annonces annonce = convertToEntity(dto);
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

        Annonces saved = annoncesRepository.save(annonce);

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

        creerDisponibiliteParDefaut(saved);

        return convertToDTO(saved);
    }

    public List<AnnonceDTO> getAllAnnonces() {
        return annoncesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AnnonceResponseDTO obtenirAnnonce(Long id) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));
        return convertirEnResponseDTO(annonce);
    }

    public Optional<AnnonceDTO> getAnnonceById(Long id) {
        return annoncesRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<AnnonceResponseDTO> obtenirAnnoncesActives() {
        List<Annonces> annonces = annoncesRepository.findByEstActive(true);
        return annonces.stream()
                .map(this::convertirEnResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceDTO> getAnnoncesActives() {
        return annoncesRepository.findAll()
                .stream()
                .filter(a -> a.getEstActive() != null && a.getEstActive())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceResponseDTO> obtenirAnnoncesProprietaire(Long proprietaireId) {
        List<Annonces> annonces = annoncesRepository.findByIdProprietaire_Id(proprietaireId);
        return annonces.stream()
                .map(this::convertirEnResponseDTO)
                .collect(Collectors.toList());
    }

    public AnnonceResponseDTO mettreAJourAnnonce(Long id, AnnonceRequestDTO request) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        annonce.setTitre(request.getTitre());
        annonce.setPrix(request.getPrix());
        annonce.setAdresse(request.getAdresse());
        annonce.setVille(request.getVille());
        annonce.setNbreChambres(request.getNbreChambres());
        annonce.setNbreLits(request.getNbreLits());
        annonce.setMaxInvites(request.getMaxInvites());
        annonce.setDescription(request.getDescription());
        annonce.setTypeAnnonce(request.getTypeAnnonce());

        annonce = annoncesRepository.save(annonce);

        return convertirEnResponseDTO(annonce);
    }

    public AnnonceDTO updateAnnonce(Long id, AnnonceDTO dto) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

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

    public AnnonceResponseDTO changerStatutAnnonce(Long id, Boolean estActive) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        annonce.setEstActive(estActive);
        annonce = annoncesRepository.save(annonce);

        return convertirEnResponseDTO(annonce);
    }

    public void activerAnnonce(Long id, Boolean activer) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée"));
        annonce.setEstActive(activer);
        annoncesRepository.save(annonce);
    }

    public void supprimerAnnonce(Long id) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        annonce.setEstActive(false);
        annoncesRepository.save(annonce);
    }

    public void deleteAnnonce(Long id) {
        annoncesRepository.deleteById(id);
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
            // Recherche avancée complète avec tous les critères
            resultats = annoncesRepository.rechercheAvanceeComplete(
                    recherche.getVille(),
                    recherche.getQuartier(),
                    recherche.getTypeAnnonce(),
                    recherche.getPrixMin(),
                    recherche.getPrixMax(),
                    recherche.getNbreChambresMin(),
                    recherche.getNbreLitsMin(),
                    recherche.getMaxInvitesMin(),
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

    // ===== NOUVELLES MÉTHODES POUR LA PAGE HOME =====

    /**
     * Récupère toutes les villes disponibles
     */
    public List<String> getVillesDisponibles() {
        List<String> villes = annoncesRepository.findAllVillesDistinct();

        // Fallback sur la table localisation si aucune ville dans annonces
        if (villes == null || villes.isEmpty()) {
            villes = localisationRepository.findAllVilles();
        }

        return villes != null ? villes : new ArrayList<>();
    }

    /**
     * Récupère les quartiers d'une ville
     */
    public List<String> getQuartiersByVille(String ville) {
        if (ville == null || ville.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> quartiers = annoncesRepository.findQuartiersByVille(ville);

        // Fallback sur la table localisation
        if (quartiers == null || quartiers.isEmpty()) {
            quartiers = localisationRepository.findQuartiersByVille(ville);
        }

        return quartiers != null ? quartiers : new ArrayList<>();
    }

    /**
     * Récupère tous les types d'annonces
     */
    public List<String> getTypesAnnonces() {
        List<String> types = annoncesRepository.findAllTypesAnnonces();

        // Si aucun type trouvé, retourner des valeurs par défaut
        if (types == null || types.isEmpty()) {
            types = Arrays.asList("Appartement", "Maison", "Studio", "Villa", "Hôtel");
        }

        return types;
    }

    /**
     * Compte le nombre d'annonces actives
     */
    public Long countAnnoncesActives() {
        Long count = annoncesRepository.countAnnoncesActives();
        return count != null ? count : 0L;
    }

    /**
     * Récupère les annonces recommandées (top annonces)
     */
    public List<AnnonceDTO> getAnnoncesRecommandees(int limit) {
        List<Annonces> annonces = annoncesRepository.findTopAnnonces();

        return annonces.stream()
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les fourchettes de prix (min, max, moyenne)
     */
    public Map<String, Double> getFourchettePrix() {
        Map<String, Double> fourchette = new HashMap<>();

        Double min = annoncesRepository.findMinPrix();
        Double max = annoncesRepository.findMaxPrix();
        Double avg = annoncesRepository.findAvgPrix();

        fourchette.put("min", min != null ? min : 0.0);
        fourchette.put("max", max != null ? max : 1000000.0);
        fourchette.put("moyenne", avg != null ? avg : 100000.0);

        return fourchette;
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

    // ===== Méthodes utilitaires privées =====

    private void creerDisponibiliteParDefaut(Annonces annonce) {
        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setIdAnnonce(annonce);
        disponibilite.setDateDebut(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        disponibilite.setDateFin(cal.getTime());

        disponibilite.setEstDisponible(true);
        disponibilite.setPrixSurcharge(0.0);

        disponibiliteRepository.save(disponibilite);
    }

    private AnnonceResponseDTO convertirEnResponseDTO(Annonces annonce) {
        AnnonceResponseDTO dto = new AnnonceResponseDTO();

        dto.setId(annonce.getId());
        dto.setTitre(annonce.getTitre());
        dto.setPrix(annonce.getPrix());
        dto.setAdresse(annonce.getAdresse());
        dto.setVille(annonce.getVille());
        dto.setNbreChambres(annonce.getNbreChambres());
        dto.setNbreLits(annonce.getNbreLits());
        dto.setMaxInvites(annonce.getMaxInvites());
        dto.setDescription(annonce.getDescription());
        dto.setTypeAnnonce(annonce.getTypeAnnonce());
        dto.setEstActive(annonce.getEstActive());
        dto.setEvaluationMoyenne(annonce.getEvaluationMoyenne());
        dto.setTotalAvis(annonce.getTotalAvis());
        dto.setUrlImagePrincipale(annonce.getUrlImagePrincipale());

        if (annonce.getIdProprietaire() != null) {
            dto.setProprietaireId(annonce.getIdProprietaire().getId());
            dto.setProprietaireNom(annonce.getIdProprietaire().getNomEntreprise());
        }

        return dto;
    }

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

        if (annonce.getUrlImages() != null && !annonce.getUrlImages().isEmpty()) {
            dto.setUrlImages(Arrays.asList(annonce.getUrlImages().split(",")));
        }

        if (annonce.getIdProprietaire() != null) {
            dto.setIdProprietaire(annonce.getIdProprietaire().getId());
            if (annonce.getIdProprietaire().getIdUser() != null) {
                dto.setProprietaireNom(annonce.getIdProprietaire().getIdUser().getNom());
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
        dto.setIdAnnonce(loc.getIdAnnonce());
        return dto;
    }
}