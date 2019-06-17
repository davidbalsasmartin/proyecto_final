import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NuevoUsuario } from '../models/nuevo-usuario';
import { Constantes } from '../utiles/constantes';

const cabecera = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

const requestParams = {headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'})};


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private registroURL = 'enter/register/';
  private confirmaEmailURL = 'enter/confirm/';
  private olvidaContraURL = 'enter/olvida/';
  private banURL = 'user/ban/';
  private usuarioModURL = 'user/updateUser/';
  private usuarioContraURL = 'user/modifica/';

  constructor(private httpClient: HttpClient) { }

  public registro(nuevoUsuario: NuevoUsuario): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.registroURL, nuevoUsuario , cabecera)
  }

  public confirmEmail(id: number, token: string): Observable<boolean> {
    return this.httpClient.get<boolean>(Constantes.REST_API + this.confirmaEmailURL + id + '/' + token);
  }

  public olvidaContra(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(Constantes.REST_API + this.olvidaContraURL + email);
  }

  public banUsuario(email: string, borraRutinas: boolean): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.banURL + email + '/' + borraRutinas, requestParams);
  }

  public usuarioMod(diasDisponibles: number, meta: string): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.usuarioModURL + diasDisponibles + '/' + meta, requestParams);
  }

  public usuarioContra(contrasena: string): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.usuarioContraURL + contrasena, requestParams);
  }
}