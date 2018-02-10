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
          dataset.backgroundColor = this._colorService.colors[index];
          tool.results.forEach((result: Result) => {
            dataset.data.push(result.Metric.MetricValue);
          });
          diagram.data.datasets.push(dataset);
        index++;
        });
        console.log(diagram.data.datasets.length);
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

}

