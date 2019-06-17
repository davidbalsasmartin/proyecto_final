import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EjercicioDTO } from '../models/ejercicio-dto';
import { EjercicioPages } from '../models/ejercicio-pages';
import { Constantes } from '../utiles/constantes';

const cabecera = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

const requestParams = {headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'})};

@Injectable({
  providedIn: 'root'
})
export class EjercicioService {

  private ejerciciosURL = 'ejercicio/todos';
  private ejerciciosPageURL = 'ejercicio/todospage';
  private buscaEjercicioURL = 'ejercicio/busca';

  
  constructor(private httpClient: HttpClient) { }

  public ejercicios(): Observable<EjercicioDTO[]> {
    return this.httpClient.get<EjercicioDTO[]>(Constantes.REST_API + this.ejerciciosURL, cabecera);
  } 

  public ejerciciosPage(page:number): Observable<EjercicioPages> {
    let params = new HttpParams().set('page', page.toString());
    var respuesta =  this.httpClient.post<EjercicioPages>(Constantes.REST_API + this.ejerciciosPageURL, params ,requestParams);
    return respuesta;  
  }

  public buscaEjercicio(id:number): Observable<EjercicioDTO> {
    var respuesta =  this.httpClient.get<EjercicioDTO>(Constantes.REST_API + this.buscaEjercicioURL + '/' + id, cabecera);
    return respuesta;
  }

}