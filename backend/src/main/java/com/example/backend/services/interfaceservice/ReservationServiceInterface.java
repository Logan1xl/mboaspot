package com.example.backend.services.interfaceservice;

import com.example.backend.dto.reservationdto.DisponibiliteRequestDTO;
import com.example.backend.dto.reservationdto.ReservationRequestDTO;
import com.example.backend.dto.reservationdto.ReservationResponseDTO;

import java.util.List;

public interface ReservationServiceInterface {

    boolean verifierDisponibilite(DisponibiliteRequestDTO request);

    ReservationResponseDTO creerReservation(ReservationRequestDTO request);

    ReservationResponseDTO obtenirReservation(Long id);

    List<ReservationResponseDTO> obtenirReservationsVoyageur(Long voyageurId);

    ReservationResponseDTO annulerReservation(Long id);

    ReservationResponseDTO confirmerReservation(Long id);

    ReservationResponseDTO obtenirParCodeConfirmation(String code);
}