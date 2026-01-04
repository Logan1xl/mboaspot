package com.example.backend.repositories;

import com.example.backend.entities.Notification;
import com.example.backend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des notifications
 * @author Suzanne lumiere
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByIdUserId(Long idUser);

    List<Notification> findByIdUserIdAndEstLueFalse(Long idUser);

    Long countByIdUserIdAndEstLueFalse(Long idUser);

    void deleteByIdUserId(Long idUser);
    /**
     * Trouver toutes les notifications d'un utilisateur
     */
    List<Notification> findByIdUser(Utilisateur idUser);
    
    /**
     * Trouver les notifications non lues d'un utilisateur
     */
    List<Notification> findByIdUserAndEstLue(Utilisateur utilisateur, Boolean estLue);
    
    /**
     * Trouver les notifications par type
     */
    List<Notification> findByTypeNotification(String typeNotification);
    
    /**
     * Trouver les notifications par titre
     */
    List<Notification> findByTitre(String titre);
    
    /**
     * Compter les notifications non lues d'un utilisateur
     */
    Long countByIdUserAndEstLue(Utilisateur utilisateur, Boolean estLue);
}
