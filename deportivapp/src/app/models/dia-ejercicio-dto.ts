import { EjercicioDTO } from './ejercicio-dto';
export class DiaEjercicioDTO {

    series:string;
    descanso:number;
    intensidad:number;
    ejercicio:EjercicioDTO;

    constructor(series:string, descanso:number, intensidad:number, ejercicio:EjercicioDTO) {
        this.series = series;
        this.descanso = descanso;
        this.intensidad = intensidad;
        this.ejercicio = ejercicio;
    }
}