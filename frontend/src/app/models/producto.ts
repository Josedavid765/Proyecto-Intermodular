export class Producto {
    constructor(
        public idProducto?: number,
        public nombre?: string,
        public precioUnitario?: number,
        public stock?: number,
        public categoriaProducto?: string,
        public unidadMedida?: string,
        public disponibilidad?: boolean,
        public imagen?: string,
        public idComercio?: number
    ) {}
}
