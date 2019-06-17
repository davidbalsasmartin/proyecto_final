import {TipoEj} from '../utiles/enum/tipo-ej';
export class NuevoUsuario {
    nombre: string;
    email: string;
    contrasena: string;
    peso: number;
    meta : string;
    kcal: number;
    diasDisponibles: number;

    constructor(nombre: string, email: string, contrasena: string, peso:number, meta:string, kcal:number, diasDisponibles:number) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.peso = peso;
        this.meta = meta;
        this.kcal = kcal;
        this.diasDisponibles = diasDisponibles;
    }
}