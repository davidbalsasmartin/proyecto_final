import { Component } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from "@angular/forms";
import { EjercicioService } from '../../services/ejercicio.service';
import { RutinaService } from '../../services/rutina.service';
import { EjercicioDTO } from '../../models/ejercicio-dto';
import { TipoEj } from '../../utiles/enum/tipo-ej';
import { InfoTipo } from '../../utiles/enum/info-tipo';
import { InfoDia } from '../../utiles/enum/info-dia';

export interface Tipo {
    name: string;
}

export interface Dia {
    name: string;
}

export interface Info {
    name: string;
    value: number;
}

@Component({
    selector: 'app-crea-rutina',
    templateUrl: './crea-rutina.html',
    styleUrls: ['./crea-rutina.css']
    })
    export class CreaRutina {

  form: FormGroup;

  isCreated = false;
  isCreateFail = false;
  errors: string[];
  loading = false;
  submitted = false;

  misEjercicios:EjercicioDTO[];


  seriesDefault: string;
  intensidadDefault: number;
  descansoDefault: number;


  misTipos: Tipo[] = [
    {name: TipoEj.Fuerza},
    {name: TipoEj.Hipertrofia},
    {name: TipoEj.Mixto},
    {name: TipoEj.Definicion},
    {name: TipoEj.Funcional}];

  misDescDias: Info[] = [
    {name: InfoTipo.ABA,  value: 2},
    {name: InfoTipo.ABC, value: 3},
    {name: InfoTipo.ABAB, value: 2},
    {name: InfoTipo.ABCD, value: 4},
    {name: InfoTipo.ABCDE, value: 5}];

  misDias: Dia[] = [
    {name: InfoDia.A},
    {name: InfoDia.B},
    {name: InfoDia.C},
    {name: InfoDia.D},
    {name: InfoDia.E}];

  constructor(private fb: FormBuilder, private ejercicioService:EjercicioService,
    private rutinaService:RutinaService) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre : ['', [Validators.required, Validators.minLength(4), Validators.maxLength(30)]],
      descripcion : ['', [Validators.maxLength(120)]],
      tipo: ['', [Validators.required, Validators.maxLength(12)]],
      descripcionDias : ['', [Validators.required, Validators.maxLength(10)]],
      dias: this.fb.array([
      ])
    });
    this.ejercicioService.ejercicios().subscribe(data=> this.misEjercicios = data);
    this.form.valueChanges.subscribe(data => this.validateDias());
  }

  initDias() {
    return this.fb.group({
      diaEjercicios: this.fb.array([
        this.initDEs(),
        this.initDEs(),
        this.initDEs()
      ])
    });
  }

  initDEs() {
    return this.fb.group({
      ejercicio: ['', [Validators.required]],
      series: ['2-3x8-10', [Validators.required, 
        Validators.pattern('[1-9]([0-9])?([-][1-9]([0-9])?)?x[1-9]([0-9])?([-][1-9]([0-9])?)?')]],
      intensidad: ['80', [Validators.required, Validators.min(20), Validators.max(100)]],
      descanso: ['60', [Validators.required, Validators.min(0), Validators.max(300)]]
    })
  }

  // Crea o elimina el FormArray días, con sus ejercicios
  onChange() {
      let descripcionDias = this.form.get('descripcionDias').value;
      let x = 0;
      if  (descripcionDias === InfoTipo.ABA || descripcionDias === InfoTipo.ABAB) {
        x = 2;
      } else if (descripcionDias === InfoTipo.ABC) {
        x = 3;
      }  else if (descripcionDias === InfoTipo.ABCD) {
        x = 4;
      }  else if (descripcionDias === InfoTipo.ABCDE) {
        x = 5;
      }

      while (x != this.dias.length) {
        if (x > this.dias.length) {
          this.addX();
        } else if (x < this.dias.length) {
          this.deleteX();
        }
      }
  }

  get dias() {
    return this.form.get('dias') as FormArray;
  }

  getDiaEjercicios(ix:number) {
      return this.dias.at(ix).get('diaEjercicios') as FormArray;
  }

  addX() {
    const control = this.dias;
    control.push(this.initDias());
  }

  deleteX() {
    const control = this.dias;
    control.removeAt(this.dias.length - 1);
  }

  addY(ix) {
    const control = this.getDiaEjercicios(ix);
    if (this.getDiaEjercicios(ix).length < 8) {
        control.push(this.initDEs());
    }
  }

  deleteY(ix) {
    const control = this.getDiaEjercicios(ix);
    if (this.getDiaEjercicios(ix).length > 3) {
        control.removeAt(this.getDiaEjercicios(ix).length - 1);
    }
  }

  formErrors = {
    dias: [{
        diaEjercicios: [{
          ejercicio: '',
          series: '',
          intensidad: '',
          descanso: ''
        }]
      }]
  };

  validationMessages = {
    dias: {
        diaEjercicios: {
        ejercicio: {
          required: 'Este ejercicio es requerido '
        },
        series: {
          required: 'Esta serie es requerida ',
          pattern: 'Se debe seguir el patrón, ej: 3-2x8-10'
        },
        intensidad: {
          required: 'Intensidad es requerida.',
          min: 'El mínimo es 20%',
          max: 'El máximo es 100%'
          },
        descanso: {
          required: 'Descanso es requerida',
          min: 'El mínimo es 0s',
          max: 'El máximo es 300s'
          }
      }
    }
  };

  // form validation
  validateDias() {
    let XsA = this.dias;

    this.formErrors.dias = [];
    let x = 1;
    while (x <= XsA.length) {
      this.formErrors.dias.push({
        diaEjercicios: [{
          ejercicio: '',
          series: '',
          intensidad: '',
          descanso: ''
        }]
      });
      let X = <FormGroup>XsA.at(x - 1);

      for (let field in X.controls) {
        let input = X.get(field);

        if (input.invalid && input.dirty) {
          for (let error in input.errors) {
            this.formErrors.dias[x - 1][field] = this.validationMessages.dias[field][error];
          }
        }
      }
      this.validateDiaEjercicios(x);
      x++;
    }

  }

  validateDiaEjercicios(x) {
    let YsA = this.getDiaEjercicios(x-1);
    this.formErrors.dias[x - 1].diaEjercicios = [];
    let y = 1;
    while (y <= YsA.length) {
      this.formErrors.dias[x - 1].diaEjercicios.push({
        ejercicio: '',
        series: '',
        intensidad: '',
        descanso: ''
      });
      let Y = <FormGroup>YsA.at(y - 1);
      for (let field in Y.controls) {
        let input = Y.get(field);
        if (input.invalid && input.dirty) {
          for (let error in input.errors) {
            this.formErrors.dias[x - 1].diaEjercicios[y - 1][field] 
            = this.validationMessages.dias.diaEjercicios[field][error];
          }
        }
      }
      y++;
    }
  }


  onCreate(): void {
    this.submitted = true;
    this.validateDias();

    // stop here if form is invalid
    if (this.form.invalid) {
        return;
    }

    this.loading = true;

    this.rutinaService.creaRutina(this.form.value).subscribe(data => {
      if (data)
        location.reload();
    },
      (err: any) => {
        this.errors = err;
        this.isCreated = false;
        this.isCreateFail = true;
      }
    );
    this.loading = false;
  }
}
