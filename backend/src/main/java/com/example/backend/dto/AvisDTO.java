package com.example.backend.dto;

import com.example.backend.entities.Voyageur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvisDTO {
    private Long id;
    private Integer note;
    private String photos;
    private Boolean estSignale;
    private String raisonSignalement;
    private Voyageur idVoyageur;
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

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Boolean getEstSignale() {
        return estSignale;
    }

    public void setEstSignale(Boolean estSignale) {
        this.estSignale = estSignale;
    }

    public String getRaisonSignalement() {
        return raisonSignalement;
    }

    public void setRaisonSignalement(String raisonSignalement) {
        this.raisonSignalement = raisonSignalement;
    }
}
