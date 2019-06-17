import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import { RutinaSearchDTO } from '../../models/rutina-search-dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'busqueda',
  templateUrl: './busqueda.html',
  styleUrls: ['./busqueda.css']
})
export class Busqueda implements OnInit {

  searchForm: FormGroup;
  rutinasNombres : RutinaSearchDTO[] = [];
  loading: boolean = false;
  errors: string[] = [];

  constructor (private rutinaService: RutinaService, private formBuilder: FormBuilder,
    private router: Router) { }
  
  ngOnInit () {
    this.searchForm = this.formBuilder.group({
      searchTerm: ['', [Validators.required, Validators.maxLength(30)]],
    });

    // busca al momento
    this.searchForm.controls.searchTerm.valueChanges.subscribe(
      nombre => {
        if (nombre != '') {
          this.rutinaService.buscaRutina(nombre).subscribe(
            data => {
              this.rutinasNombres = data;
            } , (err: any) => {
                this.errors = err;
            }); 
        }
    })
  }

  busqueda() {
    if (this.searchForm.invalid) {
      return;
    }

    this.loading = true;
    let busqueda = this.searchForm.controls.searchTerm.value;
    if (this.rutinasNombres.length === 1 && busqueda === this.rutinasNombres[0].nombre) {
      this.router.navigate(['rutina', this.rutinasNombres[0].idRutina]);
    } else {
      this.errors = ['Esa no es una rutina pública válida.'];
    }
    this.loading = false;
  }
}