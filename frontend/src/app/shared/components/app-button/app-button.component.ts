import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button [ngClass]="variantClass" [disabled]="disabled"><ng-content></ng-content></button>
  `,
  styleUrls: ['./app-button.component.scss']
})
export class AppButtonComponent {
  @Input() variant: 'primary' | 'secondary' | 'accent' | 'ghost' = 'primary';
  @Input() disabled = false;

  get variantClass() {
    return `btn-${this.variant}`;
  }
}
