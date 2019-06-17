import { Component, OnInit } from '@angular/core';
import { NuevoUsuario } from '../../models/nuevo-usuario';
import { UsuarioService } from '../../services/usuario.service';
import { TokenService } from '../../services/token.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TipoEj } from '../../utiles/enum/tipo-ej';

export interface Sexo {
  name: string;
}

export interface Meta {
  name: string;
  valor: number;
}

export interface Actividad {
  name: string;
  valor: number;
}

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  startDate = new Date(1998, 2, 9);
  minDate = new Date(1960, 0, 1);
  maxDate: Date;
  regForm: FormGroup;
  tmb = 0;
  usuario: NuevoUsuario;
  isRegFail:boolean = false;
  errors: string[];
  registered: boolean = false;
  loading: boolean = false;
  submitted: boolean = false;

  sexos: Sexo[] = [
    {name: 'Hombre'},
    {name: 'Mujer'}];

  metas: Meta[] = [
    {name: TipoEj.Fuerza, valor: 1},
    {name: TipoEj.Hipertrofia, valor: 1},
    {name: TipoEj.Mixto, valor: 1},
    {name: TipoEj.Definicion, valor: 0},
    {name: TipoEj.Funcional, valor: 1}];

  actividades: Actividad[] = [
    {name: 'Muy baja', valor: 1.2},
    {name: 'Baja (1-3 días)', valor: 1.375},
    {name: 'Normal (3-5 días)', valor: 1.55},
    {name: 'Alta (6 días)', valor: 1.725},
    {name: 'Muy alta (a diario, actividad fuerte)', valor: 1.9}];
  

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private usuarioService: UsuarioService, private tokenService: TokenService, private router: Router) { 
      
      if (this.tokenService.getToken()) { 
        this.router.navigate(['login']);
      }
     }

  ngOnInit() {
    this.maxDate = new Date();
    this.maxDate.setFullYear(this.maxDate.getFullYear()-16);

    this.regForm = this.formBuilder.group({
      nombre: ['', [Validators.required, Validators.minLength(3) ,Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email, Validators.minLength(8) ,Validators.maxLength(40)]],
      contrasena: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(24)]],
      campoMeta: ['', [Validators.required]],
      peso: ['', [Validators.required, Validators.min(40), Validators.max(160)]],
      grasa: ['', [Validators.required, Validators.min(5), Validators.max(70)]],
      fecha: ['', [Validators.required]],
      altura: ['', [Validators.required, Validators.min(140), Validators.max(230)]],
      campoSexo: ['', [Validators.required]],
      campoActividad: ['', [Validators.required]],
      numeroDias: ['', [Validators.required, Validators.min(3), Validators.max(6)]]
    });
  }

  // easy access to form fields
  get f() { return this.regForm.controls; }

  onRegister(): void {
    this.submitted = true;
    this.errors = [];

    if (this.regForm.invalid) {
      return;
    }

    this.loading = true;

     if (this.f.grasa.value > 25) {
        this.tmb = 21.6 * ((this.f.peso.value * (100 - this.f.grasa.value)) / 100) + 370;
     } else {
     
      var yearsDiff = Math.abs(Date.now() - Number(this.f.fecha.value));

      this.tmb = (10*Number(this.f.peso.value))+(6.25*Number(this.f.altura.value))-(5* Math.floor((yearsDiff / (1000 * 3600 * 24))/365))
      +((this.f.campoSexo.value.name === 'Hombre') ? +5 : -161);
     }

    this.tmb = this.tmb * Number(this.f.campoActividad.value.valor) + ((this.f.campoMeta.value.valor === 1) ? 120 : -140) ;

    this.usuario = new NuevoUsuario(this.f.nombre.value, this.f.email.value, this.f.contrasena.value,
      Number(Number(this.f.peso.value).toFixed(2)), this.f.campoMeta.value.name, Number(this.tmb.toFixed()), this.f.numeroDias.value);

    this.usuarioService.registro(this.usuario).subscribe(data => {
      this.isRegFail = false;
      this.registered = true;
    },
      (err: any) => {
        this.registered = false;
        this.errors = err;
        this.isRegFail = true;
      }
    );
    this.loading = false;
  }
}