import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="not-found"><h1>404 - Not Found</h1></div>`,
  styles: [``]
})
export class NotFoundComponent {
}
