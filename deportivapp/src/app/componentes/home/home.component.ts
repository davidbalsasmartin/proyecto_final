import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  role:string;

  constructor(private tokenService: TokenService) { }

  ngOnInit() {
      this.role = this.tokenService.getRole();
  }
}
