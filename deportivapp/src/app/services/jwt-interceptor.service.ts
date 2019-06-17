
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class JwtInterceptorService implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let autReq = req;
    const token = this.tokenService.getToken();
    if (token != null) {
      autReq = req.clone({ headers: req.headers.append('Authorization', 'Bearer ' + token) });
    }
    return next.handle(autReq);
  }

  constructor(private tokenService: TokenService) { }

}

export const JwtInterceptorProvider = [{provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true}];