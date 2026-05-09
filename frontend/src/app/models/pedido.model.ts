import { LineaPedido } from './linea-pedido.model';

export interface Pedido {
  idPedido?: number;
  fechaPedido?: string;
  gastosEnvio?: number;
  total?: number;
  estado?: string;
  direccionEntrega?: string;
  nombreComercio?: string;
  idUsuario?: number;
  idComercio?: number;
  lineas?: LineaPedido[];
}
