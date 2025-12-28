package com.example.backend.services.interfaces;

import com.example.backend.dto.DisponibiliteRequestDTO;
import com.example.backend.dto.ReservationRequestDTO;
import com.example.backend.dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationServiceInterface {

    boolean verifierDisponibilite(DisponibiliteRequestDTO request);

    ReservationResponseDTO creerReservation(ReservationRequestDTO request);

    ReservationResponseDTO obtenirReservation(Long id);

    List<ReservationResponseDTO> obtenirReservationsVoyageur(Long voyageurId);

    ReservationResponseDTO annulerReservation(Long id);

    ReservationResponseDTO confirmerReservation(Long id);

    ReservationResponseDTO obtenirParCodeConfirmation(String code);
    List<ReservationResponseDTO> obtenirTouteResvervation();
}