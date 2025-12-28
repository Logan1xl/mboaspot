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
        // 1. Vérifier que l'annonce existe et est active
        Annonces annonce = annoncesRepository.findById(request.getAnnonceId())
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));
        if(!annonce.getEstActive()) {
            return false;
        }
        // 2. vérifier les dates

        if(request.getDateDebut().isAfter(request.getDateFin())) {
            throw new RuntimeException("Date de début après la date de fin");
        }

        if(request.getDateDebut().isBefore(LocalDate.now())) {
            throw new RuntimeException("date déjà passé");
        }
        // 3. Vérifier le nombre d'invités

        if(request.getNombreInvites()>annonce.getMaxInvites()){
            return  false;
        }

        // Convertir LocalDate en Date
        Date dateDebut = Date.from(request.getDateDebut().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateFin = Date.from(request.getDateFin().atStartOfDay(ZoneId.systemDefault()).toInstant());

       // 4. vérifier qu'il existe une disponubilité qui couvre la période

        List<Disponibilite>disponibilites=disponibiliteRepository.findDisponibiliteCouvrante(request.getAnnonceId(),dateDebut,dateFin);
        if(disponibilites.isEmpty()){
            return  false;
        }

        // 5. Vérifier qu'il n'y a pas de période bloquée
        List<Disponibilite> periodesBloquees = disponibiliteRepository
                .findPeriodesBloquees(request.getAnnonceId(), dateDebut, dateFin);

        if (!periodesBloquees.isEmpty()) {
            return false;
        }

        // 6 vérifier qu'il n'y a pas de réservation chevauchante
        List<Reservation>reservationsChevaucahntes=reservationRepository.findReservationsChevauchantes(request.getAnnonceId(),dateDebut,dateFin);
        return   reservationsChevaucahntes.isEmpty();
    }

    @Override
    public ReservationResponseDTO creerReservation(ReservationRequestDTO request) {

        DisponibiliteRequestDTO dispoCheck=new DisponibiliteRequestDTO(
                request.getAnnonceId(),
                request.getDateDebut(),
                request.getDateFin(),
                request.getNombreInvites()
        );
        if(!verifierDisponibilite(dispoCheck)) {
            throw new RuntimeException("anonces non disponible");


        }

        //recuperer l'annonce et le voyageur
        Annonces annonce=annoncesRepository.findById(request.getAnnonceId())
                .orElseThrow(()->new RuntimeException("Annonce non trouvée"));

        Voyageur voyageur=voyageurRepository.findById(request.getVoyageurId())
                .orElseThrow(()->new RuntimeException("Voyageur non disponible"));

        //caluculer le prix total
        long nombreNuits = ChronoUnit.DAYS.between(request.getDateDebut(), request.getDateFin());
        Double prixTotal = nombreNuits * annonce.getPrix();

        // Générer un code de confirmation
        String codeConfirmation = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setAnnonce(annonce);
        reservation.setIdVoyageur(voyageur);
        reservation.setDateDebut(Date.from(request.getDateDebut().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setDateFin(Date.from(request.getDateFin().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservation.setNombreInvites(request.getNombreInvites());
        reservation.setPrixTotal(prixTotal);
        reservation.setCodeConfirmation(codeConfirmation);
        reservation.setStatut("EN_ATTENTE");

        reservation = reservationRepository.save(reservation);

        return convertirEnDTO(reservation);
    }

    @Override
    public ReservationResponseDTO obtenirReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        return convertirEnDTO(reservation);
    }



    @Override
    public List<ReservationResponseDTO> obtenirReservationsVoyageur(Long voyageurId) {
        List<Reservation> reservations = reservationRepository.findByIdVoyageur_IdOrderByDateDebutDesc(voyageurId);

        return reservations.stream()
                .map(this::convertirEnDTO)
                .collect(Collectors.toList());
    }




    @Override
    public ReservationResponseDTO annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if ("TERMINEE".equals(reservation.getStatut())) {
            throw new RuntimeException("Impossible d'annuler une réservation terminée");
        }

        reservation.setStatut("ANNULEE");
        reservation = reservationRepository.save(reservation);

        return convertirEnDTO(reservation);
    }

    @Override
    public ReservationResponseDTO confirmerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (!"EN_ATTENTE".equals(reservation.getStatut())) {
            throw new RuntimeException("Seules les réservations en attente peuvent être confirmées");
        }

        reservation.setStatut("CONFIRMEE");
        reservation = reservationRepository.save(reservation);

        return convertirEnDTO(reservation);
    }
    @Override
    public ReservationResponseDTO obtenirParCodeConfirmation(String code) {
        Reservation reservation = reservationRepository.findByCodeConfirmation(code)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        return convertirEnDTO(reservation);
    }

    @Override
        public List<ReservationResponseDTO> obtenirTouteResvervation() {
            return reservationRepository.findAll()
                    .stream()
                    .map(this::convertirEnDTO)
                    .collect(Collectors.toList());
        }


    // Méthode utilitaire pour convertir Reservation en DTO
    private ReservationResponseDTO convertirEnDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setId(reservation.getId());
        dto.setCodeConfirmation(reservation.getCodeConfirmation());
        dto.setNombreInvites(reservation.getNombreInvites());
        dto.setPrixTotal(reservation.getPrixTotal());
        dto.setStatut(reservation.getStatut());

        // Infos de l'annonce
        dto.setAnnonceId(reservation.getAnnonce().getId());


        // Infos du voyageur
        dto.setVoyageurId(reservation.getIdVoyageur().getId());

        return dto;
    }
}

