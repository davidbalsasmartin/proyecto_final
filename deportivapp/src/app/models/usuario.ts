import { Role } from '../utiles/enum/role';
export class Usuario {
    nombre: string;
    email: string;
    contrasena: string;
    role: string;

    constructor(nombre: string, email: string, contrasena: string, role: string) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.role = role;
    }
}