import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { APP_BASE_HREF } from '@angular/common';
import { MaterialModule } from './material-module';
import { ChartsModule } from 'ng2-charts';
import { InFoComponent } from './componentes/info/info.component';
import { ModificaRutinaComponent } from './componentes/modifica-rutina/modifica-rutina.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GraficoPesoComponent } from './componentes/grafico-peso/grafico-peso.component';
import { HomeUserComponent } from './componentes/home-user/home-user';
import { LoginComponent } from './componentes/login/login.component';
import { MisRutinasPageComponent } from './componentes/mis-rutinas-page/mis-rutinas-page.component';
import { EjerciciosPageComponent } from './componentes/ejercicios-page/ejercicios-page.component';
import { Busqueda } from './componentes/busqueda/busqueda';
import { JwtInterceptorProvider } from './services/jwt-interceptor.service';
import { ErrorInterceptorProvider } from './services/error-interceptor.service';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import 'hammerjs';
import { RegistroComponent } from './componentes/registro/registro.component';
import { EjercicioComponent } from './componentes/ejercicio/ejercicio.component';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HomeComponent } from './componentes/home/home.component';
import { UsuarioComponent } from './componentes/usuario/usuario.component';
import { UsuarioModComponent } from './componentes/usuario-mod/usuario-mod.component';
import { UsuarioContraComponent } from './componentes/usuario-contra/usuario-contra.component';
import { RutinaComponent } from './componentes/rutina/rutina.component';
import { RutinasPublicasPageComponent } from './componentes/rutinas-publicas-page/rutinas-publicas-page.component';
import { RutinasOficialesPageComponent } from './componentes/rutinas-oficiales-page/rutinas-oficiales-page.component';
import { ClickPageDirective } from './utiles/clickPage.directive';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { CreaRutina } from './componentes/crea-rutina/crea-rutina';
import { HomeAdminComponent } from './componentes/home-admin/home-admin';

@NgModule({
  declarations: [
    AppComponent,
    GraficoPesoComponent,
    CreaRutina,
    UsuarioComponent,
    InFoComponent,
    LoginComponent,
    RutinaComponent,
    HomeComponent,
    UsuarioModComponent,
    HomeUserComponent,
    ModificaRutinaComponent,
    HomeAdminComponent,
    UsuarioContraComponent,
    RegistroComponent,
    RutinasPublicasPageComponent,
    RutinasOficialesPageComponent,
    Busqueda,
    EjercicioComponent,
    EjerciciosPageComponent,
    ClickPageDirective,
    MisRutinasPageComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    ChartsModule,
    TabsModule.forRoot(),
    MatIconModule,
    CarouselModule
  ],
    providers: [JwtInterceptorProvider, ErrorInterceptorProvider,
      { provide: APP_BASE_HREF, useValue : '/' }]
  ,
  bootstrap: [AppComponent]
})

export class AppModule { }