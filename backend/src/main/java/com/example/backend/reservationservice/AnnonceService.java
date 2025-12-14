package com.example.backend.reservationservice;

import com.example.backend.entityDTO.reservationdto.AnnonceRequestDTO;
import com.example.backend.entityDTO.reservationdto.AnnonceResponseDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Disponibilite;
import com.example.backend.entities.Proprietaire;
import com.example.backend.interfaceservice.AnnonceServiceInterface;
import com.example.backend.repository.AnnoncesRepository;
import com.example.backend.repository.DisponibiliteRepository;
import com.example.backend.repository.ProprietaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnnonceService implements AnnonceServiceInterface {

    @Autowired
    private AnnoncesRepository annoncesRepository;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private DisponibiliteRepository disponibiliteRepository;

    @Override
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
        annonce.setEstActive(true);  // Active par défaut
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);

        annonce = annoncesRepository.save(annonce);

        //  CRÉER AUTOMATIQUEMENT UNE DISPONIBILITÉ PAR DÉFAUT (1 an)
        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setIdAnnonce(annonce);
        disponibilite.setDateDebut(new Date());  // Aujourd'hui

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);  // +1 an
        disponibilite.setDateFin(cal.getTime());

        disponibilite.setEstDisponible(true);
        disponibilite.setPrixSurcharge(0.0);

        disponibiliteRepository.save(disponibilite);

        return convertirEnDTO(annonce);
    }

    @Override
    public AnnonceResponseDTO obtenirAnnonce(Long id) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        return convertirEnDTO(annonce);
    }

    @Override
    public List<AnnonceResponseDTO> obtenirAnnoncesActives() {
        List<Annonces> annonces = annoncesRepository.findByEstActive(true);

        return annonces.stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnonceResponseDTO> obtenirAnnoncesProprietaire(Long proprietaireId) {
        List<Annonces> annonces = annoncesRepository.findByIdProprietaire_Id(proprietaireId);

        return annonces.stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AnnonceResponseDTO changerStatutAnnonce(Long id, Boolean estActive) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        annonce.setEstActive(estActive);
        annonce = annoncesRepository.save(annonce);

        return convertirEnDTO(annonce);
    }

    @Override
    public AnnonceResponseDTO mettreAJourAnnonce(Long id, AnnonceRequestDTO request) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Mettre à jour les champs
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

        return convertirEnDTO(annonce);
    }

    @Override
    public void supprimerAnnonce(Long id) {
        Annonces annonce = annoncesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // Désactiver au lieu de supprimer
        annonce.setEstActive(false);
        annoncesRepository.save(annonce);
    }

    // Méthode utilitaire pour convertir Annonces en DTO
    private AnnonceResponseDTO convertirEnDTO(Annonces annonce) {
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

        // Infos du propriétaire
        dto.setProprietaireId(annonce.getIdProprietaire().getId());
        dto.setProprietaireNom(annonce.getIdProprietaire().getNomEntreprise());

        return dto;
    }
}