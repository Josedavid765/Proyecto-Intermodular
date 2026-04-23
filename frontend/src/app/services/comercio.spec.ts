import { TestBed } from '@angular/core/testing';

import { Comercio } from './comercio';

describe('Comercio', () => {
  let service: Comercio;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Comercio);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
