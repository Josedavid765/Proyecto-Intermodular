export interface Pedido {
  idPedido?: number;
  fechaPedido?: string;
  nombre?: string;
  peso?: number;
  direccionRecogida?: string;
  direccionEntrega?: string;
  latitudRecogida?: number;
  longitudRecogida?: number;
  latitudEntrega?: number;
  longitudEntrega?: number;
  distancia?: number;
  estado?: string;
  idComercio?: number;
  nombreComercio?: string;
  idRepartidor?: number;
  nombreRepartidor?: string;
  valoracionComercio?: number;
  valoracionRepartidor?: number;
}
