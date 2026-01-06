// src/app/shared/components/app-button/app-button.component.ts
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button 
      [type]="type"
      [class]="'btn ' + variant"
      [disabled]="disabled"
      (click)="handleClick($event)"
    >
      <ng-content></ng-content>
    </button>
  `,
  styleUrls: ['./app-button.component.scss']
})
export class AppButtonComponent {
  @Input() variant: 'btn-primary' | 'btn-secondary' | 'btn-accent' | 'btn-ghost' = 'btn-primary';
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() disabled = false;

  handleClick(event: Event): void {
    if (this.disabled) {
      event.preventDefault();
      event.stopPropagation();
    }
  }
}