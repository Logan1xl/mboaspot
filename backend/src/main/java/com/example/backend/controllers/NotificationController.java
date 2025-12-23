package com.example.backend.controllers;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.services.implementations.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> creerNotification(@RequestBody NotificationDTO notificationDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO created = notificationService.creerNotification(notificationDTO);
            response.put("success", true);
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenirToutesLesNotifications() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> list = notificationService.obtenirToutesLesNotifications();
            response.put("success", true);
            response.put("data", list);
            response.put("count", list.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationParId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return notificationService.obtenirNotificationParId(id)
                .map(n -> {
                    response.put("success", true);
                    response.put("data", n);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "Non trouvée");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/utilisateur/{idUser}")
    public ResponseEntity<Map<String, Object>> obtenirNotificationsParUtilisateur(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NotificationDTO> list = notificationService.obtenirNotificationsParUtilisateur(idUser);
            response.put("success", true);
            response.put("data", list);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> mettreAJourNotification(@PathVariable Long id, @RequestBody NotificationDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.mettreAJourNotification(id, dto);
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PatchMapping("/{id}/lue")
    public ResponseEntity<Map<String, Object>> marquerCommeLue(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            NotificationDTO updated = notificationService.marquerCommeLue(id);
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> supprimerNotification(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            notificationService.supprimerNotification(id);
            response.put("success", true);
            response.put("message", "Supprimée");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}