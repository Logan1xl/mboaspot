package com.example.backend.services.implementations;

import com.example.backend.dto.PaiementDTO;
import com.example.backend.entities.Paiement;
import com.example.backend.entities.Reservation;
import com.example.backend.repositories.PaiementRepository;
import com.example.backend.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des paiements
 * @author Wulfrid MBONGO
 */
@Service
@Transactional
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Crée un nouveau paiement
     */
    public PaiementDTO creerPaiement(PaiementDTO paiementDTO) {
        // Validation
        if (paiementDTO.getMontant() == null || paiementDTO.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        if (paiementDTO.getIdReservation() == null) {
            throw new IllegalArgumentException("La réservation est obligatoire");
        }

        // Vérifier que la réservation existe
        Reservation reservation = reservationRepository.findById(paiementDTO.getIdReservation())
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable avec l'ID: " + paiementDTO.getIdReservation()));

        // Créer le paiement
        Paiement paiement = new Paiement();
        paiement.setMontant(paiementDTO.getMontant());
        paiement.setMethode(paiementDTO.getMethode());
        paiement.setStatut(paiementDTO.getStatut() != null ? paiementDTO.getStatut() : "EN_ATTENTE");
        paiement.setIdReservation(reservation);

        // Générer un ID de transaction unique si non fourni
        if (paiementDTO.getIdTransaction() == null || paiementDTO.getIdTransaction().isEmpty()) {
            paiement.setIdTransaction("TXN-" + UUID.randomUUID().toString());
        } else {
            paiement.setIdTransaction(paiementDTO.getIdTransaction());
        }

        paiement.setUrlRecepisse(paiementDTO.getUrlRecepisse());

        // Sauvegarder
        Paiement paiementSauvegarde = paiementRepository.save(paiement);

        return convertirEnDTO(paiementSauvegarde);
    }

    /**
     * Récupère tous les paiements
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirTousLesPaiements() {
        return paiementRepository.findAll().stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un paiement par son ID
     */
    @Transactional(readOnly = true)
    public PaiementDTO obtenirPaiementParId(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable avec l'ID: " + id));
        return convertirEnDTO(paiement);
    }

    /**
     * Récupère tous les paiements d'une réservation
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirPaiementsParReservation(Long idReservation) {
        return paiementRepository.findByIdReservation_Id(idReservation).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un paiement par son ID de transaction
     */
    @Transactional(readOnly = true)
    public PaiementDTO obtenirPaiementParIdTransaction(String idTransaction) {
        Paiement paiement = paiementRepository.findByIdTransaction(idTransaction)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable avec l'ID de transaction: " + idTransaction));
        return convertirEnDTO(paiement);
    }

    /**
     * Récupère tous les paiements par statut
     */
    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirPaiementsParStatut(String statut) {
        return paiementRepository.findByStatut(statut).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un paiement
     */
    public PaiementDTO mettreAJourPaiement(Long id, PaiementDTO paiementDTO) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable avec l'ID: " + id));

        // Mise à jour des champs modifiables
        if (paiementDTO.getMontant() != null && paiementDTO.getMontant() > 0) {
            paiement.setMontant(paiementDTO.getMontant());
        }

        if (paiementDTO.getStatut() != null && !paiementDTO.getStatut().isEmpty()) {
            paiement.setStatut(paiementDTO.getStatut());
        }

        if (paiementDTO.getUrlRecepisse() != null) {
            paiement.setUrlRecepisse(paiementDTO.getUrlRecepisse());
        }

        if (paiementDTO.getMethode() != null && !paiementDTO.getMethode().isEmpty()) {
            paiement.setMethode(paiementDTO.getMethode());
        }

        // Sauvegarder
        Paiement paiementMisAJour = paiementRepository.save(paiement);

        return convertirEnDTO(paiementMisAJour);
    }

    /**
     * Met à jour le statut d'un paiement
     */
    public PaiementDTO mettreAJourStatutPaiement(Long id, String nouveauStatut) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable avec l'ID: " + id));

        paiement.setStatut(nouveauStatut);
        Paiement paiementMisAJour = paiementRepository.save(paiement);

        return convertirEnDTO(paiementMisAJour);
    }

    /**
     * Supprime un paiement
     */
    public void supprimerPaiement(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new IllegalArgumentException("Paiement introuvable avec l'ID: " + id);
        }
        paiementRepository.deleteById(id);
    }

    /**
     * Calcule le montant total payé pour une réservation
     */
    @Transactional(readOnly = true)
    public Double calculerMontantTotalPaye(Long idReservation) {
        Double montantTotal = paiementRepository.sumMontantByReservationAndStatut(idReservation, "VALIDE");
        return montantTotal != null ? montantTotal : 0.0;
    }

    /**
     * Vérifie si une réservation est entièrement payée
     */
    @Transactional(readOnly = true)
    public boolean estEntierementPaye(Long idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable avec l'ID: " + idReservation));

        Double montantPaye = calculerMontantTotalPaye(idReservation);
        Double prixTotal = reservation.getPrixTotal();

        return prixTotal != null && montantPaye >= prixTotal;
    }

    /**
     * Convertit une entité Paiement en DTO
     */
    private PaiementDTO convertirEnDTO(Paiement paiement) {
        PaiementDTO dto = new PaiementDTO();
        dto.setId(paiement.getId());
        dto.setMontant(paiement.getMontant());
        dto.setIdTransaction(paiement.getIdTransaction());
        dto.setUrlRecepisse(paiement.getUrlRecepisse());
        dto.setMethode(paiement.getMethode());
        dto.setStatut(paiement.getStatut());

        if (paiement.getIdReservation() != null) {
            dto.setIdReservation(paiement.getIdReservation().getId());
            dto.setCodeConfirmationReservation(paiement.getIdReservation().getCodeConfirmation());

            if (paiement.getIdReservation().getIdVoyageur() != null
                    && paiement.getIdReservation().getIdVoyageur().getIdUser() != null) {
                String nomComplet = paiement.getIdReservation().getIdVoyageur().getIdUser().getPrenom()
                        + " " + paiement.getIdReservation().getIdVoyageur().getIdUser().getNom();
                dto.setNomVoyageur(nomComplet);
            }
        }

        return dto;
    }
}