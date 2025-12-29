import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="users"><h1>Users</h1></div>`,
  styles: [``]
})
export class UsersComponent {
}
