import { Component , OnInit} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Diagram } from './diagram/diagram';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { Dataset } from './diagram/dataset';
import { Option } from './diagram/option';
import { Data } from './diagram/data';
import { ColorService } from './services/color.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  inited = false;
  title = 'app';
  diagrams: Diagram[] = [];

  constructor(private _jsonService: JsonService,private _colorService: ColorService){
    this._jsonService.getResults().subscribe((benchmarks: Benchmark[]) => {
      benchmarks.forEach(benchmark => {
        let diagram = new Diagram();
        diagram.options = new Option();

        diagram.options.maintainAspectRatio = true;
        diagram.operation = benchmark.operation;
        diagram.function = benchmark.function;
        diagram.options.responsive = true;
        diagram.options.legend = {position : "right"};
        let minValue = this.getMinValue(benchmark.tool);
        let maxValue = this.getMaxValue(benchmark.tool);
        let step = this.getLogStep(minValue,maxValue);
        diagram.options.scales =  {
          yAxes: [{
            ticks: {
              callback: function(tick, index, ticks) {
               return ((index) % 2  == 0) || (index == ticks.length - 1) ? tick.toLocaleString() : null;
              },
              min: 0
            },
            scaleLabel: {
              display: true,
              labelString: benchmark.Y_Label,
            },
            type: "logarithmic"
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString: benchmark.X_Label,
              type: "linear"
            }
          }]
        }
        diagram.type = "line";
        let size: string[];
        let tools: Tool[] = benchmark.tool;
        let maxSizeTool : Tool = this.getMaxSizeTool(benchmark);
        size = this.getSizes(maxSizeTool);
        diagram.data = new Data();
        diagram.data.labels = size;
        diagram.data.datasets = new Array();
        let index = 0;
        tools.forEach((tool : Tool) => {
          let dataset: Dataset = new Dataset();
          dataset.label = tool.name;
          dataset.data = new Array();
          dataset.fill = false;
          dataset.borderColor = this._colorService.colors[index];
          dataset.backgroundColor = this._colorService.colors[index];
          tool.results.forEach((result: Result) => {
            dataset.data.push((result.Metric.MetricValue/ 100000));
          });
          diagram.data.datasets.push(dataset);
        index++;
        });
        this.diagrams.push(diagram);
      });
      this.inited = true;
    });
  }

  getSizes(tool : Tool): string []{
    let sizes: string[] = [];
    tool.results.forEach((result: Result) =>{
      sizes.push(result.Size.toString());
    });
    return sizes;
  }

  getMaxSizeTool(benchmark: Benchmark): Tool{
    let max: Tool = benchmark.tool[0];
    for(let i = 1; i<benchmark.tool.length;i++){
      if(max.results.length < benchmark.tool[i].results.length){
        max = benchmark.tool[i];
      }
    }
    return max;
  }


  ngOnInit(){
  }

  getMinValue(tools: Tool[]) : number {
    let min: number = tools[0].results[0].Metric.MetricValue/ 100000;
    for(let i = 1;i < tools.length; i++){
      if(min > tools[i].results[0].Metric.MetricValue/ 100000){
        min = tools[i].results[0].Metric.MetricValue/ 100000;
      }
    }
    return min;
  }

  getMaxValue(tools: Tool[]) : number {
    let max: number = tools[0].results[tools[0].results.length - 1].Metric.MetricValue/ 100000;
    for(let i = 1;i < tools.length; i++){
      if(max < tools[i].results[tools[i].results.length - 1].Metric.MetricValue/ 100000){
        max = tools[i].results[tools[i].results.length - 1].Metric.MetricValue/ 100000
      }
    }
    return max;
  }

  getLogStep(min: number, max:number): number{
    return Math.log(max-min) / Math.log(2);
  }

}

