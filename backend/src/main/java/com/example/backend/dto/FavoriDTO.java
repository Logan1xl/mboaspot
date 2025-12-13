package com.example.backend.dto;

import com.example.backend.entities.Annonces;
import com.example.backend.entities.Voyageur;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FavoriDTO {

    private Long id;
    private Voyageur idVoyageur;
    private Annonces idAnnonce;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Voyageur getIdVoyageur() {
        return idVoyageur;
    }

    public void setIdVoyageur(Voyageur idVoyageur) {
        this.idVoyageur = idVoyageur;
    }

    public Annonces getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(Annonces idAnnonce) {
        this.idAnnonce = idAnnonce;
    }
}
