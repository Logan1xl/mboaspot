package com.example.backend.mappers;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entities.Notification;
import com.example.backend.entities.Utilisateur;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NotificationDTO notificationDTO);
    NotificationDTO toDTO(Notification notification);

}
