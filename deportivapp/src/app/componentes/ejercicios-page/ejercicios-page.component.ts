import { Component, OnInit } from '@angular/core';
import { EjercicioService } from '../../services/ejercicio.service';
import {EjercicioPages} from '../../models/ejercicio-pages' ;

@Component({
  selector: 'ejercicios-page-app',
  templateUrl: './ejercicios-page.component.html',
  styleUrls: ['./ejercicios-page.component.css']
})
export class EjerciciosPageComponent implements OnInit {

  ejercicioPages : EjercicioPages ;
  selectedPage : number = 0;
  errors:string[] = [];
  displayedColumns: string[]  = ['nombre', 'requisito', 'tipo'];

  constructor(private ejercicioService : EjercicioService) {}

  getPageClient(page:number): void {
    this.ejercicioService.ejerciciosPage(page).subscribe(page => {
      this.ejercicioPages = page;
    }, (err: any) => {
      this.errors = err;
    }
  );
  }

  onSelect(page: number): void {
    this.selectedPage=page;
    this.getPageClient(page);
  }

  ngOnInit() {
     this.getPageClient(0);
  }
}