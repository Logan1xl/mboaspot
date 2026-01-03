@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html'
})
export class SearchBarComponent implements OnInit {
  villes: string[] = [];
  types: string[] = [];

  searchData = {
    ville: '',
    type: '',
    minPrice: '',
    maxPrice: ''
  };

  constructor(
    private annoncesService: AnnoncesService,
    private router: Router
  ) {}

  ngOnInit() {
    this.annoncesService.getVilles().subscribe(v => this.villes = v);
    this.annoncesService.getTypes().subscribe(t => this.types = t);
  }

  onSearch() {
    this.router.navigate(['/listing'], { queryParams: this.searchData });
  }
}

