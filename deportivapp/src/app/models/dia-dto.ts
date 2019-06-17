import { DiaEjercicioDTO } from './dia-ejercicio-dto';
export class DiaDTO {

    diaEjercicios:DiaEjercicioDTO[];

    constructor(diaEjercicios:DiaEjercicioDTO[]) {
        this.diaEjercicios = diaEjercicios;
    }
}