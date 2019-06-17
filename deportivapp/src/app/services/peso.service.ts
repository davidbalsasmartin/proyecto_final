import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PesoDTO } from '../models/peso-dto';
import { Constantes } from '../utiles/constantes';


const cabecera = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

const requestParams = {headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'})};


@Injectable({
  providedIn: 'root'
})
export class PesoService {

  private ultimosPesosURL = 'peso/ultimos';

  private guardaPesoURL = 'peso/guardar/';
  
  constructor(private httpClient: HttpClient) { }

  public ultimosPesos(): Observable<PesoDTO[]> {
    return this.httpClient.get<PesoDTO[]>(Constantes.REST_API + this.ultimosPesosURL, cabecera);
  } 

  public guardaPeso(peso:number): Observable<PesoDTO> {
    return this.httpClient.post<PesoDTO>(Constantes.REST_API + this.guardaPesoURL + peso, requestParams);
  }
}