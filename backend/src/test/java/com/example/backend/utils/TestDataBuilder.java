package com.example.backend.utils;

import com.example.backend.entities.*;
import com.example.backend.roles.RoleUtilisateur;

import java.util.Date;

public class TestDataBuilder {

    public static Utilisateur createUtilisateur(String nom, String email) {
        Utilisateur user = new Utilisateur();
        user.setNom(nom);
        user.setPrenom("Test");
        user.setEmail(email);
        user.setMotDePasse("password123");
        user.setNumeroTelephone("123456789");
        user.setRole(RoleUtilisateur.valueOf("VOYAGEUR"));
        user.setEstActif(true);
        return user;
    }

    public static Admin createAdmin(Utilisateur user) {
        Admin admin = new Admin();
        admin.setIdUser(user);
        admin.setDepartement("IT");
        return admin;
    }

    public static Proprietaire createProprietaire(Utilisateur user) {
        Proprietaire proprio = new Proprietaire();
        proprio.setIdUser(user);
        proprio.setTotalAnnonces(0);
        proprio.setGainsTotal(0.0);
        proprio.setEvaluationMoyenne(0.0);
        return proprio;
    }

    public static Voyageur createVoyageur(Utilisateur user) {
        Voyageur voyageur = new Voyageur();
        voyageur.setIdUser(user);
        return voyageur;
    }

    public static Annonces createAnnonce(Proprietaire proprio) {
        Annonces annonce = new Annonces();
        annonce.setTitre("Test Annonce");
        annonce.setPrix(100.0);
        annonce.setAdresse("123 Test Street");
        annonce.setVille("Douala");
        annonce.setLatitude(4.05);
        annonce.setLongitude(9.70);
        annonce.setNbreChambres(2);
        annonce.setNbreLits(2);
        annonce.setMaxInvites(4);
        annonce.setDescription("Test description");
        annonce.setTypeAnnonce("Appartement");
        annonce.setEstActive(true);
        annonce.setEvaluationMoyenne(0.0);
        annonce.setTotalAvis(0);
        annonce.setUrlImagePrincipale("https://test.com/image.jpg");
        annonce.setIdProprietaire(proprio);
        return annonce;
    }

    public static Reservation createReservation(Annonces annonce, Voyageur voyageur) {
        Reservation reservation = new Reservation();
        reservation.setIdVoyageur(voyageur);
        reservation.setAnnonce(annonce);
        reservation.setNombreInvites(2);
        reservation.setPrixTotal(300.0);
        reservation.setStatut("CONFIRMEE");
        reservation.setCodeConfirmation("TEST-" + System.currentTimeMillis());
        reservation.setDateDebut(new Date());
        reservation.setDateFin(new Date(System.currentTimeMillis() + 86400000L));
        return reservation;
    }

    public static Paiement createPaiement(Reservation reservation, Double montant) {
        Paiement paiement = new Paiement();
        paiement.setIdReservation(reservation);
        paiement.setMontant(montant);
        paiement.setMethode("CARTE");
        paiement.setStatut("VALIDE");
        paiement.setIdTransaction("TXN-" + System.currentTimeMillis());
        return paiement;
    }

    public static Signalement createSignalement(Annonces annonce, Admin admin) {
        Signalement signalement = new Signalement();
        signalement.setIdAnnonce(annonce);
        signalement.setIdAdmin(admin);
        signalement.setRaison("Contenu inapproprié");
        signalement.setDescription("Test signalement");
        signalement.setStatut("EN_ATTENTE");
        return signalement;
    }

    public static Localisation createLocalisation(Annonces annonce) {
        Localisation loc = new Localisation();
        loc.setIdAnnonce(annonce);
        loc.setVille("Douala");
        loc.setQuartier("Akwa");
        loc.setLatitude(4.05);
        loc.setLongitude(9.70);
        return loc;
    }

    public static Disponibilite createDisponibilite(Annonces annonce) {
        Disponibilite dispo = new Disponibilite();
        dispo.setIdAnnonce(annonce);
        dispo.setEstDisponible(true);
        dispo.setDateDebut(new Date());
        dispo.setDateFin(new Date(System.currentTimeMillis() + 86400000L * 7));
        dispo.setPrixSurcharge(0.0);
        return dispo;
    }

    public static Avis createAvis(Voyageur voyageur) {
        Avis avis = new Avis();
        avis.setIdVoyageur(voyageur);
        avis.setNote(4);
        avis.setEstSignale(false);
        return avis;
    }

    public static Favori createFavori(Voyageur voyageur, Annonces annonce) {
        Favori favori = new Favori();
        favori.setIdVoyageur(voyageur);
        favori.setIdAnnonce(annonce);
        return favori;
    }

    public static Notification createNotification(Utilisateur user) {
        Notification notif = new Notification();
        notif.setIdUser(user);
        notif.setTitre("Test Notification");
        notif.setMessage("Message de test");
        notif.setEstLue(false);
        notif.setTypeNotification("INFO");
        return notif;
    }

    public static Equipement createEquipement() {
        Equipement equip = new Equipement();
        equip.setNom("WiFi");
        equip.setIcone("wifi-icon");
        equip.setDescription("Internet sans fil");
        return equip;
    }
}