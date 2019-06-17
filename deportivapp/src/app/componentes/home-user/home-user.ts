import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import { RutinaPersDTO } from '../../models/rutina-pers-dto';
import { DiaEjercicioDTO } from '../../models/dia-ejercicio-dto';
import { EjercicioDTO } from '../../models/ejercicio-dto';
import { InfoTipo } from '../../utiles/enum/info-tipo';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { TipoEj } from '../../utiles/enum/tipo-ej';

export interface Macronutrientes {
  pro: number;
  hidr: number;
  gra: number;
}

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.html',
  styleUrls: ['./home-user.css'],
  providers: [
    { provide: CarouselConfig, useValue: { interval: false, noPause: true, showIndicators: true, noWrap:true } }
  ]
})
export class HomeUserComponent implements OnInit {

  macrosGenerales: Macronutrientes[] = [
    {pro: 0.25, hidr: 0.50, gra: 0.25},
    {pro: 0.32, hidr: 0.43, gra: 0.25},
    {pro: 0.30, hidr: 0.45, gra: 0.25}];

  macros: Macronutrientes;

  activeSlideIndex = 0;

  hoy:number = new Date().getDay() === 0 ? 6 : new Date().getDay() - 1;

  kcalDia:number[] = [];

  diaSemana: string[]  = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo'];

  errors:string[];
  rutinaPersDTO: RutinaPersDTO;

  displayedColumns: string[]  = ['ejercicio', 'series', 'descanso', 'intensidad'];
  arrays: any[] = [];

  constructor(private rutinaService: RutinaService) {} 

  ngOnInit() {
    this.rutinaService.homeUser().subscribe(data => {
      this.rutinaPersDTO = data;
      let diaEjercicioCardio = new DiaEjercicioDTO("1", 0, 60, new EjercicioDTO(2, "cardio moderado"));
      let diaEjercicioDescanso = new DiaEjercicioDTO("0", 0, 0, new EjercicioDTO(1, "descanso"));
      let arrayEjercicioCardio: DiaEjercicioDTO[] = [diaEjercicioCardio];
      let arrayEjercicioDescanso: DiaEjercicioDTO[] = [diaEjercicioDescanso];
      let kcal2:number;

      if (data.tipo === TipoEj.Definicion) {
        this.rutinaPersDTO.kcal -= 120;
        kcal2 = this.rutinaPersDTO.kcal;
        this.macros = this.macrosGenerales[1];
      } else if (data.tipo === TipoEj.Hipertrofia || data.tipo === TipoEj.Fuerza) {
        kcal2 = this.rutinaPersDTO.kcal + 180;
        this.macros = this.macrosGenerales[0];
      } else {
        kcal2 = this.rutinaPersDTO.kcal + 100;
        this.macros = this.macrosGenerales[2];
      }

      for (let i = 0, cardio = 0, ejercicio = 0; i < 7; i++) {
        if (data.descripcionDias === InfoTipo.ABA && i%2 == 0 && i < 6) {
          this.arrays.push(i === 2 ? data.dias[1].diaEjercicios : data.dias[0].diaEjercicios);
          this.kcalDia.push(kcal2);
          ejercicio++;
        } else if (data.descripcionDias === InfoTipo.ABAB && i < 5 && i != 2) {
          this.arrays.push(i === 0 || i === 3 ? data.dias[0] : data.dias[1]);
          this.kcalDia.push(kcal2);
        } else if (i < data.dias.length && (data.descripcionDias === InfoTipo.ABCD ||
          data.descripcionDias === InfoTipo.ABCDE)) {
          this.arrays.push(data.dias[ejercicio].diaEjercicios);
          this.kcalDia.push(kcal2);
          ejercicio++;
        } else if (data.descripcionDias === InfoTipo.ABC && i%2 == 0 && i < 6) {
          this.arrays.push(data.dias[ejercicio].diaEjercicios);
          this.kcalDia.push(kcal2);
          ejercicio++;
        }  else if (cardio < (data.numeroDias - data.descripcionDias.length)) {
          this.arrays.push(arrayEjercicioCardio);
          this.kcalDia.push(this.rutinaPersDTO.kcal);
          cardio++;
        } else {
          this.arrays.push(arrayEjercicioDescanso);
          this.kcalDia.push(this.rutinaPersDTO.kcal);

        }
      }
      
    },
      (err: any) => {
        this.errors = err;
      }
    );
    setTimeout(() => { this.activeSlideIndex = this.hoy; }, 1600);
  }
}