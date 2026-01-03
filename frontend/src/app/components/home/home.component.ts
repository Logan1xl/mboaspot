// home.component.ts
import {Router} from '@angular/router';

export class HomeComponent {
  searchParams = {
    city: '',
    priceMin: '',
    priceMax: '',
    propertyType: ''
  };

  constructor(private router: Router) {}

  handleSearch() {
    this.router.navigate(['/listing'], {
      queryParams: this.searchParams
    });
  }
}
