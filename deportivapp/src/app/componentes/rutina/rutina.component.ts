import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RutinaDTO } from '../../models/rutina-dto';
import { TokenService } from '../../services/token.service';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { UsuarioService } from '../../services/usuario.service';

@Component({
  selector: 'app-rutina',
  templateUrl: './rutina.component.html',
  styleUrls: ['./rutina.component.css'],
  providers: [
    { provide: CarouselConfig, useValue: { interval: false, noPause: true, showIndicators: true, noWrap:true } }
  ]
})
export class RutinaComponent implements OnInit {

  resultado: boolean = false;

  id:number;
  errors:string[] = [];
  goods:string[] = [];
  miRutina:RutinaDTO;
  muestraPage: boolean = false;
  role:string;
  dia: string[]  = ['A', 'B', 'C', 'D', 'E'];
  displayedColumns: string[]  = ['ejercicio', 'series', 'descanso', 'intensidad'];
  arrays = [];
  rutinaPropia: boolean = false;
  creador: string;
  loading:boolean = false;
  showMod: boolean = false;

  constructor(private rutinaService: RutinaService, private route: ActivatedRoute, private tokenService: TokenService, 
    private usuarioService: UsuarioService, private router: Router) {
        this.role = tokenService.getRole();
    if (this.route.snapshot.paramMap.get('id')) {
      if (Number(/^\d+$/.exec(this.route.snapshot.paramMap.get('id')).toString()) > 0) {
        this.id = Number(route.snapshot.paramMap.get('id'));
      } else {
        this.errors.push('No es un valor válido para una rutina.');
      }
    } else {
      this.muestraPage = true;
    }
   }

  ngOnInit() {
    if (this.id) {
      this.rutinaService.buscaRutinaPorId(this.id).subscribe(rutinaDTO => {
        this.miRutina = rutinaDTO;
        this.arrays = rutinaDTO.dias;
        this.creador = rutinaDTO.creador;
        if (rutinaDTO.idUsuario) {
          this.rutinaPropia = true;
        }
      }, (err: any) => {
        this.errors = err;
      });
    }
  }

  asigna() {
    this.errors = [];
    this.loading = true;
    this.rutinaService.asignaRutina(this.id).subscribe(data => {
      if (data) {
        this.goods = ['Se ha asignado correctamente. '];
        setTimeout(() => {
          this.router.navigate(['rutina']);
      }, 1400);
    } else {
      this.errors = ['Ha ocurrido algún problema. '];
    }}, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }

  elimina() {
    if (confirm("¿seguro que deseas eliminar esta rutina?")) {
      this.errors = [];
      this.loading = true;
      this.rutinaService.borraRutina(this.id).subscribe(data => {
        if (data) {
          this.goods = ['Se ha eliminado correctamente. ']
          setTimeout(() => {
            this.router.navigate(['rutina']);
        }, 1400);
      } else {
        this.errors = ['Ha ocurrido algún problema. '];
      }}, (err: any) => {
        this.errors = err;
      });
      this.loading = false;
    }
  }

  publica() {
    this.errors = [];
    this.loading = true;
    this.rutinaService.publicaRutina(this.id).subscribe(data => {
      if (data) {
        this.goods = ['Se ha publicado correctamente. '];
        setTimeout(() => {
          this.router.navigate(['rutina']);
      }, 1400);
    } else {
      this.errors = ['Ha ocurrido algún problema. '];
    }}, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }

  copia() {
    this.errors = [];
    this.loading = true;
    this.rutinaService.copiaRutina(this.id).subscribe(data => {
      if (data) {
        this.goods = ['Se ha copiado correctamente. '];
        setTimeout(() => {
          this.router.navigate(['rutina']);
      }, 1400);
    } else {
      this.errors = ['Ha ocurrido algún problema. '];
    }}, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }

  oficial() {
    this.errors = [];
    this.loading = true;
    this.rutinaService.oficialRutina(this.id).subscribe(data => {
      if (data) {
        this.goods = ['Se ha oficializado correctamente. '];
        setTimeout(() => {
          this.router.navigate(['rutina']);
      }, 1400);
    } else {
      this.errors = ['Ha ocurrido algún problema. '];
    }}, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }

  banUsuario() {
    if (confirm("¿seguro que deseas banear al creador de esta rutina? (" + this.creador + ")")) {
      this.ban(false);
    }
  }

  banUR() {
    if (confirm("¿seguro que deseas banear al creador (" + this.creador + ") y borrar sus rutinas?")) {
      this.ban(true);
    }
  }

  modifica() {
    this.showMod = true;
  }

  ban(borraRutinas:boolean) {
    this.errors = [];
    this.loading = true;
    this.usuarioService.banUsuario(this.creador, borraRutinas).subscribe(data => {
      if (data) {
        this.goods = ['El usuario ha sido baneado correctamente. '];
      if (data)
      setTimeout(() => {
        this.router.navigate(['rutina']);
      }, 1400);
    } else {
      this.errors = ['Ha ocurrido algún problema. '];
    }}, (err: any) => {
      this.errors = err;
    });
    this.loading = false;
  }
}