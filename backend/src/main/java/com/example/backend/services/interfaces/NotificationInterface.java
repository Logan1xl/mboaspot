package com.example.backend.services.interfaces;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entities.Utilisateur;

import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour la gestion des notifications (CRUD)
 * Basée sur les méthodes de NotificationService.java
 * @author Wulfrid MBONGO
 */
public interface NotificationInterface {
    
    /**
     * Créer une nouvelle notification
     */
    NotificationDTO creerNotification(NotificationDTO notificationDTO);
    
    /**
     * Récupérer toutes les notifications
     */
    List<NotificationDTO> obtenirToutesLesNotifications();
    
    /**
     * Récupérer une notification par ID
     */
    Optional<NotificationDTO> obtenirNotificationParId(Long id);
    
    /**
     * Récupérer les notifications d'un utilisateur
     */
    List<NotificationDTO> obtenirNotificationsParUtilisateur(Long idUser);
    
    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    List<NotificationDTO> obtenirNotificationsNonLues(Long idUser);
    
    /**
     * Compter les notifications non lues d'un utilisateur
     */
    Long compterNotificationsNonLues(Long idUser);
    
    /**
     * Marquer une notification comme lue
     */
    NotificationDTO marquerCommeLue(Long id);
    
    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     */
    void marquerToutesCommeLues(Long idUser);
    
    /**
     * Mettre à jour une notification
     */
    NotificationDTO mettreAJourNotification(Long id, NotificationDTO notificationDTO);
    
    /**
     * Supprimer une notification
     */
    void supprimerNotification(Long id);
    
    /**
     * Supprimer toutes les notifications d'un utilisateur
     */
    void supprimerToutesLesNotificationsUtilisateur(Long idUser);
}
