import { Injectable } from '@angular/core';
import { CanActivate, RouterStateSnapshot, ActivatedRouteSnapshot, Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import {Role} from '../utiles/enum/role';

@Injectable({
  providedIn: 'root'
})

export class GuardService implements CanActivate {

  realRol: string;

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    this.realRol = Role.User;
      if (this.tokenService.getRole() === 'ADMIN') {
        this.realRol = Role.Admin;
      }

    if (!this.tokenService.getToken() || route.data.roleValid.indexOf(this.realRol) === -1) {
      this.router.navigate(['login']);
      this.tokenService.logOut();
      return false;
    }
    return true;
  }

  constructor(private tokenService: TokenService, private router: Router) { }
}