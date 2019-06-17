import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RutinaPersDTO } from '../models/rutina-pers-dto';
import { RutinaDTO } from '../models/rutina-dto';
import { RutinaSearchDTO } from '../models/rutina-search-dto';
import { RutinaPages } from '../models/rutina-pages';
import { map } from 'rxjs/operators';
import { debounceTime } from 'rxjs/internal/operators/debounceTime';
import { Estadisticas } from '../models/estadisticas';
import { TokenService } from './token.service';
import { Role } from '../utiles/enum/role';
import { Constantes } from '../utiles/constantes';

const cabecera = {headers: new HttpHeaders({'Content-Type': 'application/json'})};

const requestParams = {headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'})};

@Injectable({
  providedIn: 'root'
})
export class RutinaService {

  private homeURL = 'rutina/home';
  private searchRutina = 'rutina/buscanombre/';
  private creaRutinaURL = 'rutina/creaRutina';
  private creaRutinaAdminURL = 'rutina/creaRutinaAdmin';
  private misRutinasPageURL = 'rutina/misrutinaspage';
  private rutinasPublicasURL = 'rutina/rutinaspublicas';
  private buscaRutinaURL = 'rutina/busca/';
  private modificaRutinaURL = 'rutina/modifica';
  private eliminaRutinaURL = 'rutina/elimina/';
  private asignaRutinaURL = 'rutina/asigna/';
  private asignaRutinaAutoURL = 'rutina/asignaauto';
  private publicaURL = 'rutina/publica/';
  private copiaURL = 'rutina/copia/';
  private oficialURL = 'rutina/oficial/';
  private estadisticasURL = 'rutina/estadistica';
  
  constructor(private httpClient: HttpClient, private tokenService: TokenService) { }

  public homeUser(): Observable<RutinaPersDTO> {
    return this.httpClient.get<RutinaPersDTO>(Constantes.REST_API + this.homeURL, cabecera);
  } 

  public buscaRutina(nombre:string): Observable<RutinaSearchDTO[]> {
    return this.httpClient.get<RutinaSearchDTO[]>(Constantes.REST_API + this.searchRutina + nombre ,cabecera)
    .pipe( debounceTime(500),  // esperar 500 milisegundos
      map( (data: any) => {
          return data;
      }
    )); 
  }

  public misRutinasPage(page:number): Observable<RutinaPages> {
    let params = new HttpParams()
    .set('page', page.toString());
    var respuesta =  this.httpClient.post<RutinaPages>(Constantes.REST_API + this.misRutinasPageURL, params ,requestParams);
    return respuesta;  
  }

  public rutinasPublicas(page:number, oficial:boolean): Observable<RutinaPages> {
    let params = new HttpParams().set('page', page.toString()).set('oficial', oficial.toString());
    var respuesta =  this.httpClient.post<RutinaPages>(Constantes.REST_API + this.rutinasPublicasURL, params ,requestParams);
    return respuesta;  
  }

  public creaRutina(rutina:any): Observable<boolean> {
    let url:string = this.tokenService.getRole() === Role.Admin ? this.creaRutinaAdminURL : this.creaRutinaURL;
    return this.httpClient.post<boolean>(Constantes.REST_API + url, rutina, cabecera);
  }

  public modificaRutina(rutina:RutinaDTO): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.modificaRutinaURL, rutina, cabecera);
  }

  public buscaRutinaPorId(id:number): Observable<RutinaDTO> {
    return this.httpClient.get<RutinaDTO>(Constantes.REST_API + this.buscaRutinaURL + id, cabecera);
  }

  public borraRutina(id:number): Observable<boolean> {
    return this.httpClient.delete<boolean>(Constantes.REST_API + this.eliminaRutinaURL + id, cabecera);
  }

  public asignaRutina(id:number): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.asignaRutinaURL + id, requestParams);
  }

  public publicaRutina(id:number): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.publicaURL + id, requestParams);
  }

  public copiaRutina(id:number): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.copiaURL + id, requestParams);
  }

  public oficialRutina(id:number): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.oficialURL + id, requestParams);
  }

  public asignaRutinaAuto(): Observable<boolean> {
    return this.httpClient.post<boolean>(Constantes.REST_API + this.asignaRutinaAutoURL, requestParams);
  }

  public getEstadisticas(): Observable<Estadisticas> {
    return this.httpClient.get<Estadisticas>(Constantes.REST_API + this.estadisticasURL, cabecera);
  }
}