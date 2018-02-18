import { Injectable,EventEmitter } from '@angular/core';
import { Option } from '../model/option';
import { JsonService } from '../../services/json.service';
import { Result } from '../../model/result';
import { Benchmark } from '../../model/benchmark';
import { Data } from '../model/data';
import { Tool } from '../../model/tool';
import { Dataset } from '../model/dataset';
import { ColorService } from '../../services/color.service';
import { digest } from '@angular/compiler/src/i18n/serializers/xmb';

@Injectable()
export class DiagramService {

  private _benchmarks : Array<Benchmark>
  private _event : EventEmitter<null>
  constructor(private _jsonService: JsonService, private _colorService: ColorService) {
    this._event = new EventEmitter<null>();
  console.log("new diagram")
   this._jsonService.getResults().subscribe((benchmarks: Benchmark[]) => {
    this._benchmarks = benchmarks;
    this._event.emit()
    });
   }

   get Event(){
     return this._event;
   }

   getDiagramData(title: string): Data{
     var benchmark =  this._benchmarks.find((value : Benchmark) => {
      return value.title === title
     })
     console.log(benchmark)
     if (benchmark != null || benchmark != undefined) {
      var data = new Data();
      let tools: Tool[] = benchmark.tool;
      let maxSizeTool : Tool = this.getMaxSizeTool(benchmark);
      data.labels = this.getSizes(maxSizeTool);
      let index = 0;
      data.datasets = new Array<Dataset>();
      tools.forEach((tool : Tool) => {
        data.datasets.push(this.getDataSet(tool,index));
        index++;
      });
      return data;
     } else {
       return null;
     }
    }

   private getDataSet(tool : Tool, index: number){
      let dataset: Dataset = new Dataset();
      dataset.lineTension = 0;
      dataset.label = tool.name;
      dataset.data = new Array();
      dataset.fill = false;
      dataset.borderColor = this._colorService.colors[index];
      dataset.backgroundColor = this._colorService.colors[index];
      tool.results.forEach((result: Result) => {
        dataset.data.push((result.metric.MetricValue));
      });
      return dataset;
   }

    getTitles(): Array<string>{
    var titles = new Array<string>(); 
    this._benchmarks.forEach((value: Benchmark)=>{
      titles.push(value.title);
     })
     return titles;
   }

   private getSizes(tool : Tool): string []{
    let sizes: string[] = [];
    tool.results.forEach((result: Result) =>{
      sizes.push(result.size.toString());
    });
    return sizes;
  }

  private getMaxSizeTool(benchmark: Benchmark): Tool{
    let max: Tool = benchmark.tool[0];
    for(let i = 1; i<benchmark.tool.length;i++){
      if(max.results.length < benchmark.tool[i].results.length){
        max = benchmark.tool[i];
      }
    }
    return max;
  }

  getOption(yLabel: string, xLabel: string): Option{
    return {
      maintainAspectRatio : true,
      legend : {
        position : "right"
      },
      responsive : true,
      scales: {
        yAxes: [{
          ticks: {
            callback: function(tick, index, ticks) {
             return (index % 3 == 0) || (index == ticks.length-1) ? tick.toLocaleString(): null;
            },
            min: 0            
          },
          scaleLabel: {
            display: true,
            labelString: yLabel,
          },
          type: "logarithmic"
        }],
        xAxes : [{
          scaleLabel: {
            display: true,
            labelString: xLabel
          }
        }]
      }
    }
  }
}
