import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="home"><h1>Home page </h1></div>`,
  styles: [``]
})
export class HomeComponent {
}
