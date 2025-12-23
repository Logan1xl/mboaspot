package com.example.backend.services.implementations;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entities.Notification;
import com.example.backend.entities.Utilisateur;
import com.example.backend.mappers.NotificationMapper;
import com.example.backend.repositories.NotificationRepository;
import com.example.backend.repositories.UtilisateurRepository;
import com.example.backend.services.interfaces.NotificationInterface;
import org.springframework.beans.factory.annotation.Autowired;
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
    private NotificationRepository notificationRepository;
    private UtilisateurRepository utilisateurRepository;
    private NotificationMapper  notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, UtilisateurRepository utilisateurRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDTO creerNotification(NotificationDTO notificationDTO) {
        if (notificationDTO.getId() == null || notificationDTO.getTitre() == null || notificationDTO.getMessage()== null || !utilisateurRepository.existsById(notificationDTO.getIdUser().getId())) {
            throw new IllegalArgumentException("donnee invalide");
        }else {
            try{
                return notificationMapper.toDTO(notificationRepository.save(notificationMapper.toEntity(notificationDTO)));
            } catch (Exception e) {
                throw e;
            }
        }
    }


    // ================== READ ==================

    @Override
    public List<NotificationDTO> obtenirToutesLesNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDTO> obtenirNotificationParId(Long id) {
        return notificationRepository.findById(id)
                .map(notificationMapper::toDTO);
    }

    @Override
    public List<NotificationDTO> obtenirNotificationsParUtilisateur(Long idUser) {
        if (!utilisateurRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.findByIdUser(utilisateurRepository.findById(idUser).get())
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> obtenirNotificationsNonLues(Long idUser) {
        if (!utilisateurRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.findByIdUserIdAndEstLueFalse(idUser)
                .stream()
                .map(notificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long compterNotificationsNonLues(Long idUser) {
        if (!utilisateurRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        return notificationRepository.countByIdUserIdAndEstLueFalse(idUser);
    }

    // ================== UPDATE ==================

    @Override
    public NotificationDTO marquerCommeLue(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification introuvable"));

        notification.setLue(true);
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    @Override
    public void marquerToutesCommeLues(Long idUser) {
        if (!utilisateurRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Utilisateur inexistant");
        }

        List<Notification> notifications =
                notificationRepository.findByIdUserIdAndEstLueFalse(idUser);

        notifications.forEach(n -> n.setLue(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public NotificationDTO mettreAJourNotification(Long id, NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification introuvable"));

        if (notificationDTO.getTitre() != null) {
            notification.setTitre(notificationDTO.getTitre());
        }
        if (notificationDTO.getMessage() != null) {
            notification.setMessage(notificationDTO.getMessage());
        }

        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    // ================== DELETE ==================

    @Override
    public void supprimerNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("Notification introuvable");
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public void supprimerToutesLesNotificationsUtilisateur(Long idUser) {
        if (!utilisateurRepository.existsById(idUser)) {
            throw new IllegalArgumentException("Utilisateur inexistant");
        }
        notificationRepository.deleteByIdUserId(idUser);
    }
}
