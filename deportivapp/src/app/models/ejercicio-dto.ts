import { MusculoDTO } from './musculo-dto';
export class EjercicioDTO {

    idEjercicio:number;
    nombre:string;

    constructor(idEjercicio:number, nombre:string) {
            this.idEjercicio = idEjercicio;
            this.nombre = nombre;
    }
}