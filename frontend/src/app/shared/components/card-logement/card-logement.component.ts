import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card-logement',
  templateUrl: './card-logement.component.html',
  styleUrls: ['./card-logement.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class CardLogementComponent {
  @Input() logement: any;
}
