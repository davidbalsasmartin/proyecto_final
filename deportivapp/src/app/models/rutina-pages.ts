import { RutinaPaginadoDTO } from './rutina-paginado-dto';
export class RutinaPages {
    content : RutinaPaginadoDTO[];
    totalPages : number;
    totalElements : number;
    last : boolean;
    size : number ;
    first : boolean ;
    sort : string ;
    numberOfElements : number ;
}