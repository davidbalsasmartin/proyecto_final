import { Component, OnInit } from '@angular/core';
import { TokenService } from './services/token.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  navbarOpen = false;
  touchedButton = false;
  role: string;
  constructor(private tokenService: TokenService, private router: Router) { }

  toggleNavbar() {
    if (!this.navbarOpen) {
      this.navbarOpen = true;
      this.touchedButton = true;
    } else {
      this.navbarOpen = false;
      this.touchedButton = false;
    }
  }

  closeNavbar () {
    this.touchedButton = !this.touchedButton;
    if (this.navbarOpen && this.touchedButton) {
      this.navbarOpen = false;
      this.touchedButton = false;;
    }
  }

  getURL(): string {
      return /\/[\w]+$/.exec(location.href) ? /\/[\w]+$/.exec(location.href).toString() : '';
  }

  getRole(): string {
    if (this.tokenService.getRole())
      return this.tokenService.getRole();
  }

  ngOnInit() {
    if (this.tokenService.getToken() && this.tokenService.getRole()) {
      this.role = this.tokenService.getRole();
    }
  }
}