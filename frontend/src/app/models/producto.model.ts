export interface Producto {
  idProducto?: number;
  nombre?: string;
  descripcion?: string;
  precioUnitario?: number;
  stock?: number;
  categoriaProducto?: string;
  unidadMedida?: string;
  disponibilidad?: boolean;
  imagen?: string;
  idComercio?: number;
}
