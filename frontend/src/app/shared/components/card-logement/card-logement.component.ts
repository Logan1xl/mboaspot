import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Annonce } from '../../../core/models';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-card-logement',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './card-logement.component.html',
  styleUrls: ['./card-logement.component.scss']
})
export class CardLogementComponent {
  @Input() annonce!: Annonce;
  @Input() showFavoriteBtn = true;
  @Input() isFavorite = false;
  @Output() favoriteToggle = new EventEmitter<number>();

  apiUrl = environment.apiUrl;

  getImageUrl(): string {
    if (this.annonce.urlImagePrincipale) {
      return `${environment.apiUrl}${this.annonce.urlImagePrincipale}`;
    }
    return 'assets/images/default-house.jpg';
  }

  toggleFavorite(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    this.favoriteToggle.emit(this.annonce.id);
  }

  getStars(): number[] {
    return Array(5).fill(0).map((_, i) => i);
  }

  isStarFilled(index: number): boolean {
    return index < Math.floor(this.annonce.evaluationMoyenne || 0);
  }
}
