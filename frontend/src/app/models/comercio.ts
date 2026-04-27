export class Comercio {
    constructor(
        // Cambiamos idcomercio -> idComercio para que coincida con Java
        public idComercio?: number, 
        public nombreComercio?: string,
        public categoria?: string,
        public direccionComercio?: string,
        public telefono?: string,
        public horarioApertura?: string
    ) {}
}