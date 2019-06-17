import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InFoComponent {
  constructor(private router: Router) {
   }

   suena() {
    const audio = new Audio('assets/clin.mp3');
    audio.play();
    this.router.navigate(['login']);
   }
}