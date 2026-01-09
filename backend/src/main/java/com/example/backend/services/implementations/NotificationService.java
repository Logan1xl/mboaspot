package com.example.backend.services.implementations;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entities.Notification;
import com.example.backend.entities.Utilisateur;
import com.example.backend.mappers.NotificationMapper;
import com.example.backend.repositories.NotificationRepository;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.services.interfaces.NotificationInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de gestion des notifications avec opérations CRUD
 * @author Lumiere NGO NGWA
 */
@Service
@Transactional
public class NotificationService implements NotificationInterface {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private NotificationRepository notificationRepository;
    private UtilisateurRepository utilisateurRepository;
    private NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository,
                               UtilisateurRepository utilisateurRepository,
                               NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDTO creerNotification(NotificationDTO notificationDTO) {
        log.info("Tentative de création d'une notification");

        if (notificationDTO.getId() == null
                || notificationDTO.getTitre() == null
                || notificationDTO.getMessage() == null
                || !utilisateurRepository.existsById(notificationDTO.getIdUser().getId())) {

            log.warn("Création notification refusée : données invalides");
            throw new IllegalArgumentException("donnee invalide");
        } else {
            try {
                NotificationDTO saved =
                        notificationMapper.toDTO(
                                notificationRepository.save(
                                        notificationMapper.toEntity(notificationDTO)
                                )
                        );

                log.info("Notification créée avec succès id={}", saved.getId());
                return saved;

            } catch (Exception e) {
                log.error("Erreur lors de la création de la notification", e);
                throw e;
            }
        }
    }

    // ================== READ ==================

    @Override
    public List<NotificationDTO> obtenirToutesLesNotifications() {
        log.info("Récupération de toutes les notifications");

        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDTO> obtenirNotificationParId(Long id) {
        log.info("Récupération de la notification id={}", id);

        return notificationRepository.findById(id)
                .map(notificationMapper::toDTO);
    }

    @Override
    public List<NotificationDTO> obtenirNotificationsParUtilisateur(Long idUser) {
        log.info("Récupération des notifications pour utilisateur idUser={}", idUser);

        if (!utilisateurRepository.existsById(idUser)) {
            log.warn("Utilisateur inexistant idUser={}", idUser);
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.findByIdUser(utilisateurRepository.findById(idUser).get())
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> obtenirNotificationsNonLues(Long idUser) {
        log.info("Récupération des notifications non lues idUser={}", idUser);

        if (!utilisateurRepository.existsById(idUser)) {
            log.warn("Utilisateur inexistant idUser={}", idUser);
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.findByIdUserIdAndEstLueFalse(idUser)
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long compterNotificationsNonLues(Long idUser) {
        log.info("Comptage des notifications non lues idUser={}", idUser);

        if (!utilisateurRepository.existsById(idUser)) {
            log.warn("Utilisateur inexistant idUser={}", idUser);
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.countByIdUserIdAndEstLueFalse(idUser);
    }

    // ================== UPDATE ==================

    @Override
    public NotificationDTO marquerCommeLue(Long id) {
        log.info("Marquage de la notification comme lue id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification introuvable id={}", id);
                    return new IllegalArgumentException("Notification introuvable");
                });

        notification.setLue(true);
        Notification updated = notificationRepository.save(notification);

        log.info("Notification marquée comme lue id={}", id);
        return notificationMapper.toDTO(updated);
    }

    @Override
    public void marquerToutesCommeLues(Long idUser) {
        log.info("Marquage de toutes les notifications comme lues idUser={}", idUser);

        if (!utilisateurRepository.existsById(idUser)) {
            log.warn("Utilisateur inexistant idUser={}", idUser);
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        List<Notification> notifications =
                notificationRepository.findByIdUserIdAndEstLueFalse(idUser);

        notifications.forEach(n -> n.setLue(true));
        notificationRepository.saveAll(notifications);

        log.info("Toutes les notifications marquées comme lues idUser={}", idUser);
    }

    @Override
    public NotificationDTO mettreAJourNotification(Long id, NotificationDTO notificationDTO) {
        log.info("Mise à jour de la notification id={}", id);

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification introuvable id={}", id);
                    return new IllegalArgumentException("Notification introuvable");
                });

        if (notificationDTO.getTitre() != null) {
            notification.setTitre(notificationDTO.getTitre());
        }
        if (notificationDTO.getMessage() != null) {
            notification.setMessage(notificationDTO.getMessage());
        }

        Notification updated = notificationRepository.save(notification);
        log.info("Notification mise à jour avec succès id={}", id);

        return notificationMapper.toDTO(updated);
    }

    // ================== DELETE ==================

    @Override
    public void supprimerNotification(Long id) {
        log.info("Suppression de la notification id={}", id);

        if (!notificationRepository.existsById(id)) {
            log.warn("Notification introuvable id={}", id);
            throw new IllegalArgumentException("Notification introuvable");
        }

        notificationRepository.deleteById(id);
        log.info("Notification supprimée avec succès id={}", id);
    }

    @Override
    public void supprimerToutesLesNotificationsUtilisateur(Long idUser) {
        log.info("Suppression de toutes les notifications utilisateur idUser={}", idUser);

        if (!utilisateurRepository.existsById(idUser)) {
            log.warn("Utilisateur inexistant idUser={}", idUser);
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        notificationRepository.deleteByIdUserId(idUser);
        log.info("Toutes les notifications utilisateur supprimées idUser={}", idUser);
    }
}
