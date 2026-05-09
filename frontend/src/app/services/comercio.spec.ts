import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { ComercioService } from './comercio';

describe('ComercioService', () => {
  let service: ComercioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
    });
    service = TestBed.inject(ComercioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
