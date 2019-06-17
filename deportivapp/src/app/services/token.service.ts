import { Injectable } from '@angular/core';
import {Role} from '../utiles/enum/role';

const TOKEN = 'AuthToken';
const EMAIL = 'AuthEmail';
const ROLE = 'AuthRole';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }

  public setAll(email:string, role:string, token:string) {
    this.setEmail(email);
    this.setRole(role);
    this.setToken(token);
  }

  public setToken(token: string): void {
    window.localStorage.removeItem(TOKEN);
    window.localStorage.setItem(TOKEN, token);
  }

  public getToken(): string {
    return localStorage.getItem(TOKEN);
  }

  public setEmail(email: string): void {
    window.localStorage.removeItem(EMAIL);
    window.localStorage.setItem(EMAIL, email);
  }

  public getEmail(): string {
    return localStorage.getItem(EMAIL);
  }

  public setRole(role: string): void {
    window.localStorage.removeItem(ROLE);
    window.localStorage.setItem(ROLE, role);
    if (role !== Role.Admin && role !== Role.User) {
      this.logOut();
    }
  }

  public getRole(): string {
    return localStorage.getItem(ROLE);
  }

  public logOut(): void {
    window.localStorage.clear();
  }
}