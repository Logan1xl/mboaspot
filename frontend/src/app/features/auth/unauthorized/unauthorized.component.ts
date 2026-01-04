import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="unauthorized"><h1>Unauthorized</h1></div>`,
  styles: [``]
})
export class UnauthorizedComponent {
}
