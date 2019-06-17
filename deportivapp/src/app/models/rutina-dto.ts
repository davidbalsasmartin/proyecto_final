import { DiaDTO } from './dia-dto';
export class RutinaDTO {
    idRutina:number;
    nombre : string;
    fechaSubida : Date;
    tipo : string;
    copias:number;
    creador:string;
    numeroDias:number;
    dias:DiaDTO[];
    descripcionDias:string;
    descripcion:string;
    idUsuario:string;

    constructor(idRutina:number, nombre: string, fechaSubida : Date, tipo : string, copias:number, creador:string,
        numeroDias:number, dias:DiaDTO[], descripcionDias:string, descripcion: string, idUsuario:string) {
        this.idRutina = idRutina;
        this.descripcionDias = descripcionDias;
        this.fechaSubida = fechaSubida;
        this.nombre = nombre;
        this.tipo = tipo;
        this.copias = copias;
        this.creador = creador;
        this.numeroDias = numeroDias;
        this.dias = dias;
        this.descripcion = descripcion;
        this.idUsuario = idUsuario;
    }
}