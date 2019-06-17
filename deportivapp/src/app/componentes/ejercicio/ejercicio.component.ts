import { Component, OnInit } from '@angular/core';
import { EjercicioService } from '../../services/ejercicio.service';
import { ActivatedRoute } from '@angular/router';
import { EjercicioDTO } from '../../models/ejercicio-dto';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-ejercicio',
  templateUrl: './ejercicio.component.html',
  styleUrls: ['./ejercicio.component.css']
})
export class EjercicioComponent implements OnInit {
  resultado: boolean = false;

  role:string;
  id:number;
  errors:string[] = [];
  miEjercicio:EjercicioDTO;
  muestraPage:boolean = false;

  constructor(private ejercicioService: EjercicioService, private route: ActivatedRoute, private tokenService: TokenService) {
    if (this.route.snapshot.paramMap.get('id')) {
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    } else {
      this.muestraPage = true;
    }
  }

  ngOnInit() {
    this.role = this.tokenService.getRole();
    if (this.id) {
      this.ejercicioService.buscaEjercicio(this.id).subscribe(ejercicioDTO => {
        this.miEjercicio = ejercicioDTO;
      }, (err: any) => {
        this.errors = err;
      }); 
    }
  }
}