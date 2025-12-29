import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listing',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="listing"><h1>Listing</h1></div>`,
  styles: [``]
})
export class ListingComponent {
}
