export class Pedido {
    constructor(
        public idPedido?: number,
        public fechaPedido?: string,
        public estado?: string,
        public total?: number,
        public direccionEntrega?: string,
        
        public nombreComercio?: string,
        public idComercio?: number,
        public nombreRepartidor?: string
    ) {}
}