package com.example.backend.controllers;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.services.implementations.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> creerNotification(@RequestBody NotificationDTO notificationDTO) {
        logger.info("Création d'une notification : {}", notificationDTO);
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO created = notificationService.creerNotification(notificationDTO);
            logger.info("Notification créée avec succès : {}", created);
            response.put("success", true);
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la notification : {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenirToutesLesNotifications() {
        logger.info("Récupération de toutes les notifications");
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> list = notificationService.obtenirToutesLesNotifications();
            logger.info("Nombre de notifications récupérées : {}", list.size());
            response.put("success", true);
            response.put("data", list);
            response.put("count", list.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des notifications : {}", e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationParId(@PathVariable Long id) {
        logger.info("Récupération de la notification ID : {}", id);
        Map<String, Object> response = new HashMap<>();
        return notificationService.obtenirNotificationParId(id)
                .map(n -> {
                    logger.info("Notification trouvée : {}", n);
                    response.put("success", true);
                    response.put("data", n);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    logger.warn("Notification non trouvée ID : {}", id);
                    response.put("success", false);
                    response.put("message", "Non trouvée");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/utilisateur/{idUser}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationsParUtilisateur(@PathVariable Long idUser) {
        logger.info("Récupération des notifications pour l'utilisateur ID : {}", idUser);
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> list = notificationService.obtenirNotificationsParUtilisateur(idUser);
            logger.info("Nombre de notifications récupérées pour l'utilisateur {} : {}", idUser, list.size());
            response.put("success", true);
            response.put("data", list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des notifications pour l'utilisateur {} : {}", idUser, e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> mettreAJourNotification(@PathVariable Long id, @RequestBody NotificationDTO dto) {
        logger.info("Mise à jour de la notification ID {} : {}", id, dto);
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.mettreAJourNotification(id, dto);
            logger.info("Notification mise à jour avec succès : {}", updated);
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la notification ID {} : {}", id, e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PatchMapping("/{id}/lue")
    public ResponseEntity<Map<String, Object>> marquerCommeLue(@PathVariable Long id) {
        logger.info("Marquage comme lue de la notification ID : {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.marquerCommeLue(id);
            logger.info("Notification ID {} marquée comme lue", id);
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors du marquage comme lue de la notification ID {} : {}", id, e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> supprimerNotification(@PathVariable Long id) {
        logger.info("Suppression de la notification ID : {}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.supprimerNotification(id);
            logger.info("Notification ID {} supprimée avec succès", id);
            response.put("success", true);
            response.put("message", "Supprimée");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la notification ID {} : {}", id, e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
