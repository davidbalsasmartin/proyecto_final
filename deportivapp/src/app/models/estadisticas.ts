export class Estadisticas {

    oficiales:number;
	publicas:number;
	privadas:number;
	baneado:number;
	noBaneado:number;

    constructor(oficiales:number, publicas:number, privadas:number, baneado:number, noBaneado:number) {
        this.oficiales = oficiales;
        this.publicas = publicas;
        this.privadas = privadas;
        this.baneado = baneado;
        this.noBaneado = noBaneado;
    }
}