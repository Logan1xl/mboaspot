package com.example.backend.services.implementations;

import com.example.backend.dto.AnnonceDTO;
import com.example.backend.entities.Annonces;
import com.example.backend.entities.Disponibilite;
import com.example.backend.entities.Reservation;
import com.example.backend.entities.Voyageur;
import com.example.backend.dto.DisponibiliteRequestDTO;
import com.example.backend.dto.ReservationRequestDTO;
import com.example.backend.dto.ReservationResponseDTO;
import com.example.backend.repositories.DisponibiliteRepository;
import com.example.backend.services.interfaces.ReservationServiceInterface;
import com.example.backend.repositories.AnnoncesRepository;
import com.example.backend.repositories.ReservationRepository;
import com.example.backend.repositories.VoyageurRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService implements ReservationServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AnnoncesRepository annoncesRepository;
    @Autowired
    private VoyageurRepository voyageurRepository;
    @Autowired
    DisponibiliteRepository disponibiliteRepository;

    @Override
    public boolean verifierDisponibilite(DisponibiliteRequestDTO request) {
        logger.info("Vérification disponibilité annonce ID={}", request.getAnnonceId());

        Annonces annonce = annoncesRepository.findById(request.getAnnonceId())
                .orElseThrow(() -> {
                    logger.error("Annonce non trouvée ID={}", request.getAnnonceId());
                    return new RuntimeException("Annonce non trouvée");
                });

        if (!annonce.getEstActive()) {
            logger.warn("Annonce inactive ID={}", request.getAnnonceId());
            return false;
        }

        if (request.getDateDebut().isAfter(request.getDateFin())) {
            logger.warn("Date début après date fin");
            throw new RuntimeException("Date de début après la date de fin");
        }

        if (request.getDateDebut().isBefore(LocalDate.now())) {
            logger.warn("Date de début déjà passée");
            throw new RuntimeException("date déjà passé");
        }

        if (request.getNombreInvites() > annonce.getMaxInvites()) {
            logger.warn("Nombre d'invités dépasse le maximum autorisé");
            return false;
        }

        Date dateDebut = Date.from(request.getDateDebut()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFin = Date.from(request.getDateFin()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Disponibilite> disponibilites =
                disponibiliteRepository.findDisponibiliteCouvrante(
                        request.getAnnonceId(), dateDebut, dateFin);

        if (disponibilites.isEmpty()) {
            logger.info("Aucune disponibilité couvrante trouvée");
            return false;
        }

        List<Disponibilite> periodesBloquees =
                disponibiliteRepository.findPeriodesBloquees(
                        request.getAnnonceId(), dateDebut, dateFin);

        if (!periodesBloquees.isEmpty()) {
            logger.info("Période bloquée détectée pour l'annonce ID={}", request.getAnnonceId());
            return false;
        }

        List<Reservation> reservationsChevauchantes =
                reservationRepository.findReservationsChevauchantes(
                        request.getAnnonceId(), dateDebut, dateFin);

        boolean disponible = reservationsChevauchantes.isEmpty();
        logger.info("Disponibilité finale annonce ID={} : {}", request.getAnnonceId(), disponible);

        return disponible;
    }

    @Override
    public ReservationResponseDTO creerReservation(ReservationRequestDTO request) {
        logger.info("Création réservation pour annonce ID={} et voyageur ID={}",
                request.getAnnonceId(), request.getVoyageurId());

        DisponibiliteRequestDTO dispoCheck = new DisponibiliteRequestDTO(
                request.getAnnonceId(),
                request.getDateDebut(),
                request.getDateFin(),
                request.getNombreInvites()
        );

        if (!verifierDisponibilite(dispoCheck)) {
            logger.warn("Annonce non disponible ID={}", request.getAnnonceId());
            throw new RuntimeException("anonces non disponible");
        }

        Annonces annonce = annoncesRepository.findById(request.getAnnonceId())
                .orElseThrow(() -> {
                    logger.error("Annonce non trouvée ID={}", request.getAnnonceId());
                    return new RuntimeException("Annonce non trouvée");
                });

        Voyageur voyageur = voyageurRepository.findById(request.getVoyageurId())
                .orElseThrow(() -> {
                    logger.error("Voyageur non trouvé ID={}", request.getVoyageurId());
                    return new RuntimeException("Voyageur non disponible");
                });

        long nombreNuits =
                ChronoUnit.DAYS.between(request.getDateDebut(), request.getDateFin());
        Double prixTotal = nombreNuits * annonce.getPrix();

        String codeConfirmation =
                "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Reservation reservation = new Reservation();
        reservation.setAnnonce(annonce);
        reservation.setIdVoyageur(voyageur);
        reservation.setDateDebut(Date.from(request.getDateDebut()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setDateFin(Date.from(request.getDateFin()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setNombreInvites(request.getNombreInvites());
        reservation.setPrixTotal(prixTotal);
        reservation.setCodeConfirmation(codeConfirmation);
        reservation.setStatut("EN_ATTENTE");

        reservation = reservationRepository.save(reservation);

        logger.info("Réservation créée avec succès ID={}, Code={}",
                reservation.getId(), reservation.getCodeConfirmation());

        return convertirEnDTO(reservation);
    }

    @Override
    public ReservationResponseDTO obtenirReservation(Long id) {
        logger.info("Récupération réservation ID={}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Réservation non trouvée ID={}", id);
                    return new RuntimeException("Réservation non trouvée");
                });

        return convertirEnDTO(reservation);
    }

    @Override
    public List<ReservationResponseDTO> obtenirReservationsVoyageur(Long voyageurId) {
        logger.info("Récupération réservations du voyageur ID={}", voyageurId);

        return reservationRepository
                .findByIdVoyageur_IdOrderByDateDebutDesc(voyageurId)
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponseDTO annulerReservation(Long id) {
        logger.info("Annulation réservation ID={}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Réservation non trouvée ID={}", id);
                    return new RuntimeException("Réservation non trouvée");
                });

        if ("TERMINEE".equals(reservation.getStatut())) {
            logger.warn("Tentative d'annulation d'une réservation terminée ID={}", id);
            throw new RuntimeException("Impossible d'annuler une réservation terminée");
        }

        reservation.setStatut("ANNULEE");
        reservation = reservationRepository.save(reservation);

        logger.info("Réservation annulée ID={}", id);

        return convertirEnDTO(reservation);
    }

    @Override
    public ReservationResponseDTO confirmerReservation(Long id) {
        logger.info("Confirmation réservation ID={}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Réservation non trouvée ID={}", id);
                    return new RuntimeException("Réservation non trouvée");
                });

        if (!"EN_ATTENTE".equals(reservation.getStatut())) {
            logger.warn("Statut invalide pour confirmation réservation ID={}", id);
            throw new RuntimeException(
                    "Seules les réservations en attente peuvent être confirmées");
        }

        reservation.setStatut("CONFIRMEE");
        reservation = reservationRepository.save(reservation);

        logger.info("Réservation confirmée ID={}", id);

        return convertirEnDTO(reservation);
    }

    @Override
    public ReservationResponseDTO obtenirParCodeConfirmation(String code) {
        logger.info("Recherche réservation par code confirmation={}", code);

        Reservation reservation = reservationRepository.findByCodeConfirmation(code)
                .orElseThrow(() -> {
                    logger.error("Réservation non trouvée code={}", code);
                    return new RuntimeException("Réservation non trouvée");
                });

        return convertirEnDTO(reservation);
    }

    @Override
    public List<ReservationResponseDTO> obtenirTouteResvervation() {
        logger.info("Récupération de toutes les réservations");

        return reservationRepository.findAll()
                .stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }

    private ReservationResponseDTO convertirEnDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setId(reservation.getId());
        dto.setCodeConfirmation(reservation.getCodeConfirmation());
        dto.setNombreInvites(reservation.getNombreInvites());
        dto.setPrixTotal(reservation.getPrixTotal());
        dto.setStatut(reservation.getStatut());
        dto.setAnnonceId(reservation.getAnnonce().getId());
        dto.setVoyageurId(reservation.getIdVoyageur().getId());

        return dto;
    }
}
