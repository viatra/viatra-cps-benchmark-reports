import { Component , OnInit} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  inited = false;
  type = 'line';
  data = {
    labels: [],
    datasets: []
  };
  options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      yAxes: [{
          ticks: {
              beginAtZero: true,
              steps: 10,
              stepValue: 6
              }
          }]
      }
  };
  title = 'app';

  constructor(private _jsonService: JsonService){
    this._jsonService.getResults().subscribe((data: Benchmark[]) => {
      console.log(data);
      let size = [];
      let value = [];
      data[2].tool[1].results.forEach(result =>{
        size.push(result.Size);
        value.push(result.Metric.MetricValue);
      });
      this.data.labels = size;
      this.data.datasets.push({
        label: data[2].function,
        data: value
      });
      this.inited = true;
    });
  }
  ngOnInit(){
  }

}

