import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './componentes/login/login.component';
import { HomeComponent } from './componentes/home/home.component';
import { RegistroComponent } from './componentes/registro/registro.component';
import { GraficoPesoComponent } from './componentes/grafico-peso/grafico-peso.component';
import { EjercicioComponent } from './componentes/ejercicio/ejercicio.component';
import { GuardService } from './services/guard.service';
import {Role} from './utiles/enum/role';
import { RutinaComponent } from './componentes/rutina/rutina.component';
import { UsuarioComponent } from './componentes/usuario/usuario.component';
import { InFoComponent } from './componentes/info/info.component';


const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'home', component: HomeComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'login', component: LoginComponent},
  {path: 'login/:id/:token', component: LoginComponent},
  {path: 'registro', component: RegistroComponent},
  {path: 'info', component: InFoComponent},
  {path: 'usuario', component: UsuarioComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'peso', component: GraficoPesoComponent, canActivate: [GuardService], data: { roleValid: [Role.User]}},
  {path: 'ejercicio', component: EjercicioComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'ejercicio/:id', component: EjercicioComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'rutina', component: RutinaComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: 'rutina/:id', component: RutinaComponent, canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
  {path: '**', redirectTo: 'home', pathMatch: 'full', canActivate: [GuardService], data: { roleValid: [Role.Admin, Role.User]}},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
