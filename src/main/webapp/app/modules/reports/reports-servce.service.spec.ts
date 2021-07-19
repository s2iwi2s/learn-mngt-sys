import { TestBed } from '@angular/core/testing';

import { ReportsServceService } from './reports-servce.service';

describe('ReportsServceService', () => {
  let service: ReportsServceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportsServceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
