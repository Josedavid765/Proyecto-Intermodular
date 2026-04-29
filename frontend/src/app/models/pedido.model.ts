export interface Pedido {
  idPedido?: number;
  fechaPedido?: string; // ISO date string
  gastosEnvio?: number;
  total?: number;
  estado?: string;
  direccionEntrega?: string;
  idUsuario?: number;
  idComercio?: number;
  lineas?: LineaPedido[];
}
