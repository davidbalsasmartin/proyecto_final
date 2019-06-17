import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, BaseChartDirective, Label } from 'ng2-charts';
import * as pluginAnnotations from 'chartjs-plugin-annotation';
import { PesoService } from '../../services/peso.service';
import { PesoDTO } from '../../models/peso-dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-grafico-peso',
  templateUrl: './grafico-peso.component.html',
  styleUrls: ['./grafico-peso.component.css']
})
export class GraficoPesoComponent implements OnInit {
  errors:string[] = [];
  goods:string[] = [];
  pesoForm: FormGroup;
  submitted:boolean = false;
  lineChartData: ChartDataSets[] = [ { data: [] }
  ];
  lineChartLabels: Label[] = [];
  lineChartOptions: (ChartOptions & { annotation: any }) = {
    responsive: true,
    elements: {
      point: {
        radius: 4
      }
    },
    scales: {
      xAxes: [{
        gridLines: {
          display: false
        },
        ticks: {
          fontColor: 'white',
          fontSize: 18
        }
      }],
      yAxes: [
        {
          id: 'y-axis',
          position: 'left',
          gridLines: {
            color: 'rgba(255,0,0,0.8)',
            lineWidth: 2
          },
          ticks: {
            fontColor: 'white',
            fontSize: 18,
            suggestedMin: 70,
            suggestedMax: 90,
            stepSize: 5
          }
        }
      ]
    },
    annotation: {},
  };

  lineChartColors: Color[] = [
    {
      backgroundColor: 'rgba(255,255,255,0.5)',
      borderColor: 'white',
      pointBackgroundColor: 'yellow',
      pointBorderColor: 'yellow',
      pointHoverBackgroundColor: 'red',
      pointHoverBorderColor: 'red'
    }
  ];
  lineChartType = 'line';
  lineChartPlugins = [pluginAnnotations];

  @ViewChild(BaseChartDirective) chart: BaseChartDirective;

  constructor(private pesoService:PesoService, private formBuilder:FormBuilder) { }

  ngOnInit() {
    this.pesoService.ultimosPesos().subscribe(data => data.forEach(peso =>{
      this.pushOne(peso, true);
    }));
    this.pesoForm = this.formBuilder.group({
      peso: ['', [Validators.required, Validators.min(40), Validators.max(140)]]
    });
  }

  getDateFromApi(fecha:Date): string {
    let diaMenosUno = Number(fecha.toString().substring(8,10));
    let dia:string = diaMenosUno < 9 ? 0+''+(diaMenosUno+1) : (diaMenosUno+1).toString();
    let mes = fecha.toString().substring(5,7);
    let ano = fecha.toString().substring(0,4);
      return dia + "-" + mes + "-" + ano;
  }

  getDate(fecha:Date): string {
      let diaMenosUno = Number(fecha.toString().substring(8,10));
      let dia:string = diaMenosUno < 9 ? 0+''+diaMenosUno : diaMenosUno.toString();
      let mes = fecha.toString().substring(5,7);
      let ano = fecha.toString().substring(0,4);
        return dia + "-" + mes + "-" + ano;
    }

  pushOne(miPeso:PesoDTO, fechaApi:boolean) {
    let data = this.lineChartData[0].data as Number[];
    data.push(miPeso.peso);
    if (fechaApi) {
      this.lineChartLabels.push(`${this.getDateFromApi(miPeso.fecha)}`);
    } else {
      this.lineChartLabels.push(`${this.getDate(miPeso.fecha)}`);
    }
  }

  deleteFirst() {
    this.lineChartData[0].data.splice(0,1);
    this.lineChartLabels.splice(0,1);
  }

  guardaPeso() {
    this.errors = [];
    this.goods = [];
    this.submitted = true;
    // stop here if form is invalid
    if (this.pesoForm.invalid) {
        return;
    }

    this.pesoService.guardaPeso(this.pesoForm.value.peso).subscribe(data => {
      this.pushOne(data, false);
      if (this.lineChartLabels[8]) {
        this.deleteFirst();
      }
      this.goods = ['Se ha guardado correctamente.'];
    },
      (err: any) => {
        this.errors = err;
      }
    );
  }
}