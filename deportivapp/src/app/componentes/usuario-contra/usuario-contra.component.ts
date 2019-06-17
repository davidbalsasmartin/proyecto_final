import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-usuario-contra',
  templateUrl: './usuario-contra.component.html',
  styleUrls: ['./usuario-contra.component.css']
})
export class UsuarioContraComponent implements OnInit {
  contraForm: FormGroup;
  errors: string[];
  loading = false;
  submitted = false;
  goods :string[] = [];

  constructor(private formBuilder: FormBuilder, private usuarioService: UsuarioService) { 
    }

  ngOnInit() {
    this.contraForm = this.formBuilder.group({
      contrasena: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(24)]]
    });
  }

  cambio(): void {
    this.submitted = true;
    this.goods = [];
    this.errors = [];

    // stop here if form is invalid
    if (this.contraForm.invalid) {
        return;
    }
    
    this.loading = true;

    this.usuarioService.usuarioContra(this.contraForm.value.contrasena).subscribe( data => {
      if (data) {
        this.goods.push('La contrasena ha sido cambiada correctamente. ');
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
