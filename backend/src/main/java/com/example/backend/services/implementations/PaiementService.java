package com.example.backend.services.implementations;

import com.example.backend.dto.PaiementDTO;
import com.example.backend.entities.Paiement;
import com.example.backend.entities.Reservation;
import com.example.backend.repositories.PaiementRepository;
import com.example.backend.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(PaiementService.class);

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Crée un nouveau paiement
     */
    public PaiementDTO creerPaiement(PaiementDTO paiementDTO) {
        logger.info("Début création paiement pour la réservation ID={}", paiementDTO.getIdReservation());

        if (paiementDTO.getMontant() == null || paiementDTO.getMontant() <= 0) {
            logger.warn("Montant invalide pour le paiement : {}", paiementDTO.getMontant());
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        if (paiementDTO.getIdReservation() == null) {
            logger.warn("Tentative de création paiement sans réservation");
            throw new IllegalArgumentException("La réservation est obligatoire");
        }

        Reservation reservation = reservationRepository.findById(paiementDTO.getIdReservation())
                .orElseThrow(() -> {
                    logger.error("Réservation introuvable ID={}", paiementDTO.getIdReservation());
                    return new IllegalArgumentException(
                            "Réservation introuvable avec l'ID: " + paiementDTO.getIdReservation());
                });

        Paiement paiement = new Paiement();
        paiement.setMontant(paiementDTO.getMontant());
        paiement.setMethode(paiementDTO.getMethode());
        paiement.setStatut(paiementDTO.getStatut() != null ? paiementDTO.getStatut() : "EN_ATTENTE");
        paiement.setIdReservation(reservation);

        if (paiementDTO.getIdTransaction() == null || paiementDTO.getIdTransaction().isEmpty()) {
            paiement.setIdTransaction("TXN-" + UUID.randomUUID());
            logger.debug("ID transaction généré automatiquement");
        } else {
            paiement.setIdTransaction(paiementDTO.getIdTransaction());
        }

        paiement.setUrlRecepisse(paiementDTO.getUrlRecepisse());

        Paiement paiementSauvegarde = paiementRepository.save(paiement);
        logger.info("Paiement créé avec succès ID={}, Transaction={}",
                paiementSauvegarde.getId(), paiementSauvegarde.getIdTransaction());

        return convertirEnDTO(paiementSauvegarde);
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirTousLesPaiements() {
        logger.info("Récupération de tous les paiements");
        return paiementRepository.findAll().stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaiementDTO obtenirPaiementParId(Long id) {
        logger.info("Recherche paiement par ID={}", id);

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Paiement introuvable ID={}", id);
                    return new IllegalArgumentException("Paiement introuvable avec l'ID: " + id);
                });

        return convertirEnDTO(paiement);
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirPaiementsParReservation(Long idReservation) {
        logger.info("Récupération des paiements pour la réservation ID={}", idReservation);

        return paiementRepository.findByIdReservation_Id(idReservation).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaiementDTO obtenirPaiementParIdTransaction(String idTransaction) {
        logger.info("Recherche paiement par ID transaction={}", idTransaction);

        Paiement paiement = paiementRepository.findByIdTransaction(idTransaction)
                .orElseThrow(() -> {
                    logger.error("Paiement introuvable pour la transaction={}", idTransaction);
                    return new IllegalArgumentException(
                            "Paiement introuvable avec l'ID de transaction: " + idTransaction);
                });

        return convertirEnDTO(paiement);
    }

    @Transactional(readOnly = true)
    public List<PaiementDTO> obtenirPaiementsParStatut(String statut) {
        logger.info("Récupération des paiements avec statut={}", statut);

        return paiementRepository.findByStatut(statut).stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    public PaiementDTO mettreAJourPaiement(Long id, PaiementDTO paiementDTO) {
        logger.info("Mise à jour paiement ID={}", id);

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Paiement introuvable ID={}", id);
                    return new IllegalArgumentException("Paiement introuvable avec l'ID: " + id);
                });

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

        Paiement paiementMisAJour = paiementRepository.save(paiement);
        logger.info("Paiement mis à jour avec succès ID={}", id);

        return convertirEnDTO(paiementMisAJour);
    }

    public PaiementDTO mettreAJourStatutPaiement(Long id, String nouveauStatut) {
        logger.info("Mise à jour du statut paiement ID={} vers {}", id, nouveauStatut);

        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Paiement introuvable ID={}", id);
                    return new IllegalArgumentException("Paiement introuvable avec l'ID: " + id);
                });

        paiement.setStatut(nouveauStatut);
        Paiement paiementMisAJour = paiementRepository.save(paiement);

        return convertirEnDTO(paiementMisAJour);
    }

    public void supprimerPaiement(Long id) {
        logger.info("Suppression paiement ID={}", id);

        if (!paiementRepository.existsById(id)) {
            logger.error("Tentative de suppression paiement inexistant ID={}", id);
            throw new IllegalArgumentException("Paiement introuvable avec l'ID: " + id);
        }

        paiementRepository.deleteById(id);
        logger.info("Paiement supprimé avec succès ID={}", id);
    }

    @Transactional(readOnly = true)
    public Double calculerMontantTotalPaye(Long idReservation) {
        logger.debug("Calcul du montant total payé pour réservation ID={}", idReservation);

        Double montantTotal =
                paiementRepository.sumMontantByReservationAndStatut(idReservation, "VALIDE");

        return montantTotal != null ? montantTotal : 0.0;
    }

    @Transactional(readOnly = true)
    public boolean estEntierementPaye(Long idReservation) {
        logger.info("Vérification paiement complet pour réservation ID={}", idReservation);

        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> {
                    logger.error("Réservation introuvable ID={}", idReservation);
                    return new IllegalArgumentException(
                            "Réservation introuvable avec l'ID: " + idReservation);
                });

        Double montantPaye = calculerMontantTotalPaye(idReservation);
        Double prixTotal = reservation.getPrixTotal();

        return prixTotal != null && montantPaye >= prixTotal;
    }

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
            dto.setCodeConfirmationReservation(
                    paiement.getIdReservation().getCodeConfirmation());

            if (paiement.getIdReservation().getIdVoyageur() != null
                    && paiement.getIdReservation().getIdVoyageur().getIdUser() != null) {

                String nomComplet =
                        paiement.getIdReservation().getIdVoyageur().getIdUser().getPrenom()
                                + " "
                                + paiement.getIdReservation().getIdVoyageur().getIdUser().getNom();
                dto.setNomVoyageur(nomComplet);
            }
        }

        return dto;
    }
}
