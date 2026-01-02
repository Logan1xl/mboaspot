import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card-logement',
  templateUrl: './card-logement.component.html',
  styleUrls: ['./card-logement.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class CardLogementComponent {
  /**
   * `logement` : objet contenant les informations de l'annonce
   * Exemple attendu : { id, title, prix, noteMoyenne, localisation: { ville }, images: [] }
   */
  @Input() logement: any;

  /** Indique si le composant est en état de chargement */
  @Input() loading = false;

  /** Emet un événement lorsque l'utilisateur clique sur le bouton favoris */
  @Output() favorite = new EventEmitter<any>();

  /**
   * Méthode appelée au clic sur le bouton favoris.
   * On stoppe la propagation pour éviter la navigation sur la carte.
   */
  onFavorite(event: Event) {
    event.stopPropagation();
    this.favorite.emit(this.logement);
  }
}
