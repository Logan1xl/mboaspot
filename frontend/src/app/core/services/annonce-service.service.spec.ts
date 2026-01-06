import { TestBed } from '@angular/core/testing';

import { AnnonceServiceService } from './annonce-service.service';

describe('AnnonceServiceService', () => {
  let service: AnnonceServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnnonceServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
