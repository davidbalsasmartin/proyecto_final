import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import {RutinaPages} from '../../models/rutina-pages' ;

@Component({
  selector: 'rutinas-oficiales-page-app',
  templateUrl: './rutinas-oficiales-page.component.html',
  styleUrls: ['./rutinas-oficiales-page.component.css']
})
export class RutinasOficialesPageComponent implements OnInit {

  constructor(private rutinaService : RutinaService) {}

  ngOnInit() {
     this.getPageClient(0, true);
  }

  displayedColumns: string[]  = ['nombre', 'tipo', 'descripcion', 'copias'];
  rutinaPages : RutinaPages ;
  selectedPage : number = 0;

  getPageClient(page:number, oficial: boolean): void {
    this.rutinaService.rutinasPublicas(page, oficial).subscribe(page => this.rutinaPages = page);
  }

  onSelect(page: number): void {
    this.selectedPage=page;
    this.getPageClient(page, true);
  }
}