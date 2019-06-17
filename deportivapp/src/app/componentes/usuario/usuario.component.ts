import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';
import { Router } from '@angular/router';
import { RutinaService } from '../../services/rutina.service';

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {
  role:string;
  goods:string[] = [];
  errors:string[] = [];
  loading:boolean = false;

  constructor(private tokenService: TokenService, private router: Router, 
  private rutinaService: RutinaService){ }

  ngOnInit() {
      this.role = this.tokenService.getRole();
  }

  logOut() {
    this.tokenService.logOut();
    this.role = '';
    this.router.navigate(['login']);
  }

  asignaAuto() {
    this.loading = true;
    this.goods = [];
    this.errors = [];
    this.rutinaService.asignaRutinaAuto().subscribe(data => {
      if (data){
        this.goods = ['Has cambiado de rutina pública de forma satisfactoria. '];
      } else {
        this.goods = ['Ha habido algún error. '];
      }
    }, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }
}
