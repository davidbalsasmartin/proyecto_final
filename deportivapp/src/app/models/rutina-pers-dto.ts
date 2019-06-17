import { DiaDTO } from './dia-dto';
export class RutinaPersDTO {
    nombre : string;
    fechaFinal : Date;
    tipo : string;
    kcal:number;
    numeroDias:number;
    dias:DiaDTO[];
    descripcionDias:string;

    constructor(nombre: string, fechaFinal : Date, tipo : string, kcal:number, numeroDias:number,
        dias:DiaDTO[], descripcionDias:string) {
        this.descripcionDias = descripcionDias;
        this.nombre = nombre;
        this.tipo = tipo;
        this.kcal = kcal;
        this.numeroDias = numeroDias;
        this.dias = dias;
        this.fechaFinal = fechaFinal;
    }
}