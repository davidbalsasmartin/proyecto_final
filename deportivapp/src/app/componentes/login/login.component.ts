import { Component, OnInit } from '@angular/core';
import { LoginUsuario } from '../../models/login-usuario';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  usuario: LoginUsuario;
  loadingContra = false;
  errors: string[];
  loading = false;
  submitted = false;
  goods :string[] = [];
  optionalId : number;
  optionalToken : string;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, 
    private tokenService: TokenService, private router: Router, private usuarioService: UsuarioService,
    private route: ActivatedRoute) { 

      if (this.tokenService.getToken()) { 
        this.router.navigate(['home']);
      }
      this.optionalId = Number(this.route.snapshot.paramMap.get('id'));
      this.optionalToken = this.route.snapshot.paramMap.get('token');
    }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email, Validators.minLength(8) ,Validators.maxLength(40)]],
      contrasena: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(24)]]
    });
    if (!location.href.endsWith('login')) {
    this.usuarioService.confirmEmail(this.optionalId, this.optionalToken).subscribe(data => {
      if (data) {
        this.goods = ['¡Enhorabuena, la cuenta ya está confirmada, ya puedes loguearte!'];
      } else {
        this.errors = ['Ha habido algún problema, por favor, si estás seguro de que te registraste con ese email' +
        ' y no está confirmado, revísa de nuevo la bandeja de entrada. '];
      }
        this.router.navigate(['login']);
    }, (err: any) => {
      this.errors = err;
      this.loading = false;
      setTimeout(() => {
        this.router.navigate(['login']);
    }, 2000);
    });
    }
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  olvido() {
    this.loadingContra = true;
    this.errors = [];
    this.goods = [];
    
    let validaEmail = new RegExp(/^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/);
    let email:string = this.f.email.value;
    if (email && (email.length >= 6) && (email.length <= 24) && (validaEmail.test(email))) {
      this.usuarioService.olvidaContra(email).subscribe(data => {
        if (data) {
          this.goods.push('Por favor, revisa el email y sigue los pasos para recuperar el acceso a la cuenta.');
        }
      },
      (err: any) => {
        this.errors = err;
      });  
    } else if (!email) {
      this.goods = ['Por favor, introduzca su email correctamente y vuelva a pulsar el botón.'];
    } else {
      this.errors = ['Debe introducir un email válido.'];
    }
    this.loadingContra = false;
  }

  onLogin(): void {
    this.submitted = true;
    this.goods = [];

    // stop here if form is invalid
    if (this.loginForm.invalid) {
        return;
    }
    
    this.loading = true;
    this.usuario = new LoginUsuario(this.f.email.value , this.f.contrasena.value);

    this.authService.login(this.usuario).subscribe(data => {
      this.tokenService.setAll(data.email, data.role, data.token);

      this.router.navigate(['home']);
    }, (err: any) => {
        this.errors = err;
        this.loading = false;
      }
    );
  }
}