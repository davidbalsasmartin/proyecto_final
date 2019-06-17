import { DiaDTO } from './dia-dto';
export class RutinaSearchDTO {
    idRutina : number;
    nombre : string;

    constructor(idRutina:number, nombre: string) {
        this.idRutina = idRutina;
        this.nombre = nombre;
    }
}