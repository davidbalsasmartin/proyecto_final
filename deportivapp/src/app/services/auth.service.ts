import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginUsuario } from '../models/login-usuario';
import { Observable } from 'rxjs';
import { Jwt } from '../models/jwt';
import { Constantes } from '../utiles/constantes';

const cabecera = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private URL = 'enter/login/';

  constructor(private httpClient: HttpClient) { }

  public login(usuario: LoginUsuario): Observable<Jwt> {
    return this.httpClient.post<Jwt>(Constantes.REST_API + this.URL, usuario, cabecera);
  }

}