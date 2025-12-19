package com.example.backend.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.services.implementations.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.NotificationDTO;


/**
 * Contrôleur REST pour la gestion des notifications
 * @author Wulfrid MBONGO
 */
@RestController
@RequestMapping("api/notifications")

public class NotificationController {

    NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * CREATE - Créer une nouvelle notification
     * POST /notifications
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> creerNotification(@RequestBody NotificationDTO notificationDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO created = notificationService.creerNotification(notificationDTO);
            response.put("success", true);
            response.put("message", "Notification créée avec succès");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la création de la notification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * READ - Récupérer toutes les notifications
     * GET /notifications
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenirToutesLesNotifications() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> notifications = notificationService.obtenirToutesLesNotifications();
            response.put("success", true);
            response.put("message", "Notifications récupérées avec succès");
            response.put("data", notifications);
            response.put("count", notifications.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la récupération des notifications: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * READ - Récupérer une notification par ID
     * GET /notifications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationParId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return notificationService.obtenirNotificationParId(id)
                    .map(notification -> {
                        response.put("success", true);
                        response.put("message", "Notification trouvée");
                        response.put("data", notification);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        response.put("success", false);
                        response.put("message", "Notification non trouvée avec l'ID: " + id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    });
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la récupération de la notification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * READ - Récupérer les notifications d'un utilisateur
     * GET /notifications/utilisateur/{idUser}
     */
    @GetMapping("/utilisateur/{idUser}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationsParUtilisateur(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> notifications = notificationService.obtenirNotificationsParUtilisateur(idUser);
            response.put("success", true);
            response.put("message", "Notifications de l'utilisateur récupérées avec succès");
            response.put("data", notifications);
            response.put("count", notifications.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * READ - Récupérer les notifications non lues d'un utilisateur
     * GET /notifications/utilisateur/{idUser}/non-lues
     */
    @GetMapping("/utilisateur/{idUser}/non-lues")
    public ResponseEntity<Map<String, Object>> obtenirNotificationsNonLues(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> notifications = notificationService.obtenirNotificationsNonLues(idUser);
            Long count = notificationService.compterNotificationsNonLues(idUser);
            response.put("success", true);
            response.put("message", "Notifications non lues récupérées avec succès");
            response.put("data", notifications);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * UPDATE - Mettre à jour une notificationg
     * PUT /notifications/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> mettreAJourNotification(
            @PathVariable Long id,
            @RequestBody NotificationDTO notificationDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.mettreAJourNotification(id, notificationDTO);
            response.put("success", true);
            response.put("message", "Notification mise à jour avec succès");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la mise à jour: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * UPDATE - Marquer une notification comme lue
     * PATCH /notifications/{id}/lue
     */
    @PatchMapping("/{id}/lue")
    public ResponseEntity<Map<String, Object>> marquerCommeLue(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.marquerCommeLue(id);
            response.put("success", true);
            response.put("message", "Notification marquée comme lue");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * UPDATE - Marquer toutes les notifications d'un utilisateur comme lues
     * PATCH /notifications/utilisateur/{idUser}/tout-lire
     */
    @PatchMapping("/utilisateur/{idUser}/tout-lire")
    public ResponseEntity<Map<String, Object>> marquerToutesCommeLues(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.marquerToutesCommeLues(idUser);
            response.put("success", true);
            response.put("message", "Toutes les notifications ont été marquées comme lues");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * DELETE - Supprimer une notification
     * DELETE /notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> supprimerNotification(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.supprimerNotification(id);
            response.put("success", true);
            response.put("message", "Notification supprimée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la suppression: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * DELETE - Supprimer toutes les notifications d'un utilisateur
     * DELETE /notifications/utilisateur/{idUser}
     */
    @DeleteMapping("/utilisateur/{idUser}")
    public ResponseEntity<Map<String, Object>> supprimerToutesLesNotificationsUtilisateur(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.supprimerToutesLesNotificationsUtilisateur(idUser);
            response.put("success", true);
            response.put("message", "Toutes les notifications de l'utilisateur ont été supprimées");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * SPECIAL - Envoyer une notification automatique pour une réservation (mock)
     * POST /notifications/envoi/reservation
     */
   /* @PostMapping("/envoi/reservation")
    public ResponseEntity<Map<String, Object>> envoyerNotificationReservation(@RequestBody ReservationDTO reservation) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.envoyerNotificationReservation(reservation);
            response.put("success", true);
            response.put("message", "Notification de réservation envoyée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de l'envoi de la notification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

    /**
     * SPECIAL - Envoyer une notification de rappel
     * POST /notifications/envoi/rappel
     */
   /* @PostMapping("/envoi/rappel")
    public ResponseEntity<Map<String, Object>> envoyerNotificationRappel(@RequestBody ReservationDTO reservation) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.envoyerNotificationRappel(reservation);
            response.put("success", true);
            response.put("message", "Notification de rappel envoyée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de l'envoi du rappel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

    /**
     * SPECIAL - Envoyer une notification d'annulation
     * POST /notifications/envoi/annulation
     */
    /*@PostMapping("/envoi/annulation")
    public ResponseEntity<Map<String, Object>> envoyerNotificationAnnulation(@RequestBody ReservationDTO reservation) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.envoyerNotificationAnnulation(reservation);
            response.put("success", true);
            response.put("message", "Notification d'annulation envoyée avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de l'envoi de l'annulation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }*/

}