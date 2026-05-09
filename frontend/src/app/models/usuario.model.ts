export interface Usuario {
  idUsuario?: number;
  nombre?: string;
  apellidos?: string;
  email?: string;
  password?: string;
  direccionEntrega?: string;
  telefono?: string;
  rol?: 'USUARIO' | 'REPARTIDOR' | 'COMERCIO' | 'ADMIN';
}
