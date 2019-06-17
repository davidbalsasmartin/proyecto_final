import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {Router} from "@angular/router";

import { TokenService } from './token.service';

@Injectable()
export class ErrorInterceptorService implements HttpInterceptor {
    constructor(private tokenService: TokenService, private router:Router) { }

    error :string[];

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 403 || err.status === 0 || (err.status === 401 && !location.href.endsWith('login'))) {
                this.tokenService.logOut();
                this.router.navigate(['/login']);
            }

            this.error = [];

            if (err.error.hasOwnProperty('errors')) {
                err.error.errors.forEach(myError => {
                    this.error.push(myError.field + ' : ' + myError.defaultMessage);
                });
            } else if (err.hasOwnProperty('message')) {
                this.error.push(err.error.message);
            }

            return throwError(this.error);
        }))
    }
}

export const ErrorInterceptorProvider = [{provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true}];