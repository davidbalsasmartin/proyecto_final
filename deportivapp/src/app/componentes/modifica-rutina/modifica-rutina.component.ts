import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RutinaDTO } from '../../models/rutina-dto';

@Component({
  selector: 'app-modifica-rutina',
  templateUrl: './modifica-rutina.component.html',
  styleUrls: ['./modifica-rutina.component.css'],
  providers: []
})
export class ModificaRutinaComponent implements OnInit {

  constructor(private rutinaService: RutinaService, private route: ActivatedRoute, private formBuilder: FormBuilder,
    private router: Router) {
  }

   modForm: FormGroup;
   loading = false;
   errors: string[] = [];
   submitted = false;
   id:number;
 
   ngOnInit() {
     this.modForm = this.formBuilder.group({
       nombre: ['', [Validators.required, Validators.minLength(6) ,Validators.maxLength(30)]],
       descripcion: ['', [Validators.maxLength(120)]]
     });
     this.id = Number(this.route.snapshot.paramMap.get('id'));
   }
 
   // convenience getter for easy access to form fields
   get f() { return this.modForm.controls; }
 
   onMod(): void {
     this.submitted = true;
     this.errors = [];
 
     // stop here if form is invalid
     if (this.modForm.invalid) {
         return;
     }
     
     this.loading = true;
     let rutina = new RutinaDTO(this.id , this.f.nombre.value, null, null, null, null, null, null,
      null, this.f.descripcion.value, null);
 
     this.rutinaService.modificaRutina(rutina).subscribe(data => {
      if (data) {
        location.reload();
      } else {
        this.errors.push('Ha ocurrido un error.');
      }
       
     }, (err: any) => {
         this.errors = err;
       }
     );
     this.loading = false;
   }
 }