import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { PedidoService } from './pedido';

describe('PedidoService', () => {
  let service: PedidoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
    });
    service = TestBed.inject(PedidoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
