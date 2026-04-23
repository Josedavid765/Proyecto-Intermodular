export class Usuario {
    constructor(
        public idUsuario?: number,
        public nombre?: string,
        public apellido?: string,
        public email?: string,
        public password?: string,
        public telefono?: string,
        public direccionEntrega?: string,
        public rol?: string
    ) {}
}