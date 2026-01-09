package com.example.backend.services.implementations;

import com.example.backend.dto.*;
import com.example.backend.entities.*;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnonceService {

    // Initialisation du Logger
    private static final Logger log = LoggerFactory.getLogger(AnnonceService.class);

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
        log.info("Début création annonce (RequestDTO) pour Propriétaire ID: {}", request.getProprietaireId());

        // Vérifier que le propriétaire existe
        Proprietaire proprietaire = proprietaireRepository.findById(request.getProprietaireId())
                .orElseThrow(() -> {
                    log.error("Erreur création annonce : Propriétaire ID {} non trouvé", request.getProprietaireId());
                    return new RuntimeException("Propriétaire non trouvé");
                });

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
        log.info("Annonce sauvegardée avec succès. ID généré: {}", annonce.getId());

        // Créer la localisation si ville et quartier sont fournis
        if (request.getVille() != null) {
            log.debug("Ajout de la localisation pour l'annonce ID: {}", annonce.getId());
            Localisation localisation = new Localisation();
            localisation.setVille(request.getVille());
            localisation.setQuartier(request.getQuartier() != null ? request.getQuartier() : "Centre");
            localisation.setLatitude(request.getLatitude());
            localisation.setLongitude(request.getLongitude());
            localisation.setIdAnnonce(annonce);
            localisationRepository.save(localisation);
        }

        creerDisponibiliteParDefaut(annonce);

        log.info("Fin création annonce ID: {}", annonce.getId());
        return convertirEnResponseDTO(annonce);
    }

    public AnnonceDTO createAnnonce(AnnonceDTO dto) {
        log.info("Début createAnnonce (DTO) - Titre: {}", dto.getTitre());

        Annonces annonce = convertToEntity(dto);
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

        Annonces saved = annoncesRepository.save(annonce);
        log.info("Annonce DTO sauvegardée. ID: {}", saved.getId());

        if (dto.getLocalisation() != null) {
            log.debug("Traitement localisation DTO pour annonce ID: {}", saved.getId());
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
        log.info("Récupération de toutes les annonces (getAllAnnonces)");
        List<AnnonceDTO> result = annoncesRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        log.info("Nombre total d'annonces trouvées: {}", result.size());
        return result;
    }

    public AnnonceResponseDTO obtenirAnnonce(Long id) {
        log.info("Recherche annonce (ResponseDTO) ID: {}", id);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Annonce ID {} non trouvée lors de l'obtention", id);
                    return new RuntimeException("Annonce non trouvée");
                });
        return convertirEnResponseDTO(annonce);
    }

    public Optional<AnnonceDTO> getAnnonceById(Long id) {
        log.info("Recherche annonce (Optional DTO) ID: {}", id);
        return annoncesRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<AnnonceResponseDTO> obtenirAnnoncesActives() {
        log.info("Récupération des annonces actives (ResponseDTO)");
        List<Annonces> annonces = annoncesRepository.findByEstActive(true);
        log.info("Nombre d'annonces actives trouvées: {}", annonces.size());
        return annonces.stream()
                .map(this::convertirEnResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceDTO> getAnnoncesActives() {
        log.info("Récupération des annonces actives (DTO)");
        return annoncesRepository.findAll()
                .stream()
                .filter(a -> a.getEstActive() != null && a.getEstActive())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceResponseDTO> obtenirAnnoncesProprietaire(Long proprietaireId) {
        log.info("Récupération annonces pour Propriétaire ID: {}", proprietaireId);
        List<Annonces> annonces = annoncesRepository.findByIdProprietaire_Id(proprietaireId);
        log.info("Nombre d'annonces trouvées pour ce propriétaire: {}", annonces.size());
        return annonces.stream()
                .map(this::convertirEnResponseDTO)
                .collect(Collectors.toList());
    }

    public AnnonceResponseDTO mettreAJourAnnonce(Long id, AnnonceRequestDTO request) {
        log.info("Mise à jour annonce ID: {}", id);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Impossible de mettre à jour : Annonce ID {} introuvable", id);
                    return new RuntimeException("Annonce non trouvée");
                });

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
        log.info("Annonce ID {} mise à jour avec succès", id);

        return convertirEnResponseDTO(annonce);
    }

    public AnnonceDTO updateAnnonce(Long id, AnnonceDTO dto) {
        log.info("Update annonce (DTO) ID: {}", id);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Impossible d'update : Annonce ID {} introuvable", id);
                    return new RuntimeException("Annonce non trouvée");
                });

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
        log.info("Fin update annonce (DTO) ID: {}", id);
        return convertToDTO(updated);
    }

    public AnnonceResponseDTO changerStatutAnnonce(Long id, Boolean estActive) {
        log.info("Changement statut annonce ID: {} vers estActive={}", id, estActive);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        annonce.setEstActive(estActive);
        annonce = annoncesRepository.save(annonce);

        return convertirEnResponseDTO(annonce);
    }

    public void activerAnnonce(Long id, Boolean activer) {
        log.info("Activation/Désactivation (void) annonce ID: {} -> {}", id, activer);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée"));
        annonce.setEstActive(activer);
        annoncesRepository.save(annonce);
    }

    public void supprimerAnnonce(Long id) {
        log.info("Demande de suppression logique (désactivation) annonce ID: {}", id);
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Echec suppression : Annonce ID {} introuvable", id);
                    return new RuntimeException("Annonce non trouvée");
                });

        annonce.setEstActive(false);
        annoncesRepository.save(annonce);
        log.info("Annonce ID {} désactivée avec succès", id);
    }

    public void deleteAnnonce(Long id) {
        log.info("Demande de suppression physique (deleteById) annonce ID: {}", id);
        annoncesRepository.deleteById(id);
        log.info("Annonce ID {} supprimée de la base", id);
    }

    // ===== Recherche avancée =====

    public List<AnnonceDTO> rechercherAnnonces(RechercheDTO recherche) {
        log.info("Recherche avancée initiée. Ville: {}, Type: {}", recherche.getVille(), recherche.getTypeAnnonce());
        List<Annonces> resultats;

        if (recherche.getLatitude() != null && recherche.getLongitude() != null && recherche.getRayon() != null) {
            log.debug("Recherche par géolocalisation: Lat={}, Long={}, Rayon={}",
                    recherche.getLatitude(), recherche.getLongitude(), recherche.getRayon());
            // Recherche par géolocalisation
            resultats = annoncesRepository.findAnnoncesProches(
                    recherche.getLatitude(),
                    recherche.getLongitude(),
                    recherche.getRayon()
            );
        } else {
            log.debug("Recherche classique par critères");
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

        log.info("Résultats recherche: {} annonce(s) trouvée(s)", resultats.size());
        return resultats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnnonceDTO> getTopAnnonces() {
        log.info("Récupération des Top Annonces");
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
        log.info("Récupération des villes disponibles");
        List<String> villes = annoncesRepository.findAllVillesDistinct();

        // Fallback sur la table localisation si aucune ville dans annonces
        if (villes == null || villes.isEmpty()) {
            log.debug("Aucune ville trouvée dans Annonces, recherche dans Localisation");
            villes = localisationRepository.findAllVilles();
        }

        return villes != null ? villes : new ArrayList<>();
    }

    /**
     * Récupère les quartiers d'une ville
     */
    public List<String> getQuartiersByVille(String ville) {
        log.debug("Récupération quartiers pour la ville: {}", ville);
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
        log.info("Récupération des types d'annonces");
        List<String> types = annoncesRepository.findAllTypesAnnonces();

        // Si aucun type trouvé, retourner des valeurs par défaut
        if (types == null || types.isEmpty()) {
            log.debug("Aucun type trouvé en base, retour valeurs par défaut");
            types = Arrays.asList("Appartement", "Maison", "Studio", "Villa", "Hôtel");
        }

        return types;
    }

    /**
     * Compte le nombre d'annonces actives
     */
    public Long countAnnoncesActives() {
        Long count = annoncesRepository.countAnnoncesActives();
        log.info("Comptage annonces actives: {}", count);
        return count != null ? count : 0L;
    }

    /**
     * Récupère les annonces recommandées (top annonces)
     */
    public List<AnnonceDTO> getAnnoncesRecommandees(int limit) {
        log.info("Récupération des {} annonces recommandées", limit);
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
        log.info("Calcul des fourchettes de prix");
        Map<String, Double> fourchette = new HashMap<>();

        Double min = annoncesRepository.findMinPrix();
        Double max = annoncesRepository.findMaxPrix();
        Double avg = annoncesRepository.findAvgPrix();

        fourchette.put("min", min != null ? min : 0.0);
        fourchette.put("max", max != null ? max : 1000000.0);
        fourchette.put("moyenne", avg != null ? avg : 100000.0);

        log.debug("Fourchette prix: {}", fourchette);
        return fourchette;
    }

    // ===== Gestion Disponibilité =====

    public List<DisponibiliteDTO> getDisponibilites(Long annonceId) {
        log.info("Récupération disponibilités pour annonce ID: {}", annonceId);
        return disponibiliteRepository.findByIdAnnonceId(annonceId)
                .stream()
                .map(this::convertDisponibiliteToDTO)
                .collect(Collectors.toList());
    }

    public DisponibiliteDTO addDisponibilite(DisponibiliteDTO dto) {
        log.info("Ajout disponibilité pour annonce ID: {}", dto.getIdAnnonce());
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
        log.debug("Vérification disponibilité annonce ID: {} du {} au {}", annonceId, dateDebut, dateFin);
        List<Disponibilite> dispos = disponibiliteRepository.findDisponibilitePourPeriode(
                annonceId, dateDebut, dateFin
        );
        boolean indisponible = !dispos.isEmpty();
        log.debug("Résultat vérification: {}", indisponible ? "Indisponible" : "Disponible");
        return indisponible;
    }

    // ===== Gestion Localisation =====

    public Optional<LocalisationDTO> getLocalisation(Long annonceId) {
        log.info("Récupération localisation pour annonce ID: {}", annonceId);
        return localisationRepository.findByIdAnnonceId(annonceId)
                .map(this::convertLocalisationToDTO);
    }

    public LocalisationDTO updateLocalisation(Long annonceId, LocalisationDTO dto) {
        log.info("Mise à jour localisation pour annonce ID: {}", annonceId);
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
        log.debug("Création disponibilité par défaut pour annonce ID: {}", annonce.getId());
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
        // Pas de log ici pour éviter de spammer la console lors des listings
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
                    .orElseThrow(() -> {
                        log.error("Conversion DTO->Entity : Propriétaire ID {} non trouvé", dto.getIdProprietaire());
                        return new RuntimeException("Propriétaire non trouvé");
                    });
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