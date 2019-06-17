export class RutinaPaginadoDTO {
    idRutina:number;
    nombre : string;
    copias:number;
    tipo : string;
    descripcionDias:string;
    creador:string;

    constructor(idRutina:number, nombre : string, copias:number, tipo : string, descripcionDias:string, creador:string) {
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.copias = copias;
        this.tipo = tipo;
        this.descripcionDias = descripcionDias;
        this.creador = creador;
    }
}