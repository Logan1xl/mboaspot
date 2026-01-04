package com.example.backend.dto;


import com.example.backend.entities.Utilisateur;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO pour les notifications
 * @author Lumière NGO NGWA
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO  {
    private Long id;
    private String titre;
    private String message;
    private Boolean estLue;
    private String typeNotification;
    private Utilisateur idUser;


    public void setLue(boolean b) {
    }



}
