import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import {RutinaPages} from '../../models/rutina-pages' ;

@Component({
  selector: 'rutinas-publicas-page-app',
  templateUrl: './rutinas-publicas-page.component.html',
  styleUrls: ['./rutinas-publicas-page.component.css']
})
export class RutinasPublicasPageComponent implements OnInit {

  constructor(private rutinaService : RutinaService) {}

  ngOnInit() {
     this.getPageClient(0, false);
  }
  displayedColumns: string[]  = ['nombre', 'tipo', 'descripcion', 'copias'];
  rutinaPages : RutinaPages ;
  selectedPage : number = 0;

  getPageClient(page:number, oficial: boolean): void {
    this.rutinaService.rutinasPublicas(page, oficial).subscribe(page => this.rutinaPages = page);
  }

  onSelect(page: number): void {
    this.selectedPage=page;
    this.getPageClient(page, false);
  }
}