import { TestBed } from '@angular/core/testing';

import { NotificationServiceService } from './notif-service.service';

describe('NotificationServiceService', () => {
  let service: NotificationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
