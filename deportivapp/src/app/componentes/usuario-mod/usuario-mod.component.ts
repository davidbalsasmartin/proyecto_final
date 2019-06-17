import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { TipoEj } from '../../utiles/enum/tipo-ej';

export interface Meta {
  name: string;
}

@Component({
  selector: 'app-usuario-mod',
  templateUrl: './usuario-mod.component.html',
  styleUrls: ['./usuario-mod.component.css']
})
export class UsuarioModComponent implements OnInit {
  modForm: FormGroup;
  errors: string[];
  loading = false;
  submitted = false;
  goods :string[] = [];

  metas: Meta[] = [
    {name: TipoEj.Fuerza},
    {name: TipoEj.Hipertrofia},
    {name: TipoEj.Mixto},
    {name: TipoEj.Definicion},
    {name: TipoEj.Funcional}];

  constructor(private formBuilder: FormBuilder, private usuarioService: UsuarioService) { 
    }

  ngOnInit() {
    this.modForm = this.formBuilder.group({
      campoMeta: ['', [Validators.required]],
      numeroDias: ['', [Validators.required, Validators.min(3), Validators.max(6)]]    });
  }

  modifica(): void {
    this.submitted = true;
    this.goods = [];
    this.errors = [];

    // stop here if form is invalid
    if (this.modForm.invalid) {
        return;
    }
    
    this.loading = true;

    this.usuarioService.usuarioMod(this.modForm.value.numeroDias, this.modForm.value.campoMeta.name)
    .subscribe( data => {
      if (data) {
        this.goods.push('Han sido cambiadas correctamente tus preferencias . ');
      } else {
        this.errors.push('Lo sentimos, ha habido algún error. ');
      }
    }, (err: any) => {
        this.errors = err;
      }
    );
    this.loading = false;
  }
}
