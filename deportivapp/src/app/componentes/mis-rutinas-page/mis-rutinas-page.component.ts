import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import {RutinaPages} from '../../models/rutina-pages' ;

@Component({
  selector: 'mis-rutinas-page-app',
  templateUrl: './mis-rutinas-page.component.html',
  styleUrls: ['./mis-rutinas-page.component.css']
})
export class MisRutinasPageComponent implements OnInit {

  rutinaPages : RutinaPages;
  selectedPage : number = 0;
  displayedColumns: string[]  = ['nombre', 'tipo', 'descripcion', 'copias'];
  errors:string[] = [];


  constructor(private rutinaService : RutinaService) {}

  getPageClient(page:number): void {
    this.rutinaService.misRutinasPage(page).subscribe(page => this.rutinaPages = page);
  }

  onSelect(page: number): void {
    this.selectedPage=page;
    this.getPageClient(page);
  }
  ngOnInit() {
     this.getPageClient(0);
  }
}