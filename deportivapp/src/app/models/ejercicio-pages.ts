import { EjercicioDTO } from './ejercicio-dto';
export class EjercicioPages {
    content : EjercicioDTO[];
    totalPages : number;
    totalElements : number;
    last : boolean;
    size : number ;
    first : boolean ;
    sort : string ;
    numberOfElements : number ;
}