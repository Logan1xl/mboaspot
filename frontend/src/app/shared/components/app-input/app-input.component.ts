import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule],
  template: `
    <label *ngIf="label">{{ label }}</label>
    <input [type]="type" [placeholder]="placeholder" [disabled]="disabled" />
  `,
  styleUrls: ['./app-input.component.scss']
})
export class AppInputComponent {
  @Input() label?: string;
  @Input() placeholder?: string;
  @Input() type: string = 'text';
  @Input() disabled: boolean = false;
}
