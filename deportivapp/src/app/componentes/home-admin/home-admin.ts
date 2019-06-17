import { Component, OnInit } from '@angular/core';
import { RutinaService } from '../../services/rutina.service';
import { ChartType, ChartOptions } from 'chart.js';
import { Label } from 'ng2-charts';


@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.html',
  styleUrls: ['./home-admin.css']
})
export class HomeAdminComponent implements OnInit {
  chartOptions = {
    responsive: true
  };

  errors:string[] = [];

  constructor(private rutinaService: RutinaService) {}

  public pieChartOptions: ChartOptions = {
    responsive: true,
    legend: {
      position: 'right',
      labels: {
        fontColor: 'white',
        fontSize: 18
      },
    }
  };
  public pieChartLabels: Label[] = ['Publicas', 'Oficiales', 'Privadas'];
  public pieChartData: number[] = [];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartColors = [
    {
      backgroundColor: [],
    },
  ];

  public pieChartLabels2: Label[] = ['Baneados', 'No baneados'];
  public pieChartData2: number[] = [];
  public pieChartColors2 = [
    {
      backgroundColor: [],
    },
  ];

  ngOnInit() {
    this.rutinaService.getEstadisticas().subscribe(data => {
      this.pieChartData = [data.publicas, data.oficiales, data.privadas];
      this.pieChartColors[0].backgroundColor = ['rgba(255,175,0,0.6)', 'rgba(255,55,54,0.6)', 'rgba(255,255,0,0.6)'];
      this.pieChartData2 = [data.baneado, data.noBaneado];
      this.pieChartColors2[0].backgroundColor = ['rgba(255,175,0,0.6)', 'rgba(255,55,54,0.6)'];
    }, (err: any) => {
      this.errors = err;
    }
  );
  }
}