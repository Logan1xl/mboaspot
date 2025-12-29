import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppButtonComponent } from '../../shared/components/app-button/app-button.component';
import { AppCardComponent } from '../../shared/components/app-card/app-card.component';
import { AppInputComponent } from '../../shared/components/app-input/app-input.component';

@Component({
  selector: 'app-design-demo',
  standalone: true,
  imports: [CommonModule, AppButtonComponent, AppCardComponent, AppInputComponent],
  templateUrl: './design-demo.component.html',
  styleUrls: ['./design-demo.component.scss']
})
export class DesignDemoComponent {}
