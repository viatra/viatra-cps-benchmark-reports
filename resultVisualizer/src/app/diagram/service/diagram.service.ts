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
import { Diagram } from '../model/diagram';
import { query } from '@angular/core/src/render3/instructions';
import { Console } from '@angular/core/src/console';
import { Scenario } from '../../model/scenario';
import { Observable } from 'rxjs/Observable';
import { take } from 'rxjs/operator/take';
import { Config } from '../../model/config';
import { Color } from '../../model/color';
import { ResultConfig } from '../../model/resultConfig';
import { ConfigService } from '../../services/config.service';
import { Build } from '../../model/build';
import { ResultsData } from '../../model/resultData';

@Injectable()
export class DiagramService {
  private _selectedTitle: string;
  private _benchmarks : Array<Benchmark>;
  private _selectionUpdate : EventEmitter<SelectionUpdateEvent>;
  private _initEvent : EventEmitter<null>;
  private _diagrams : Array<Diagram>;
  private _scenarios : Array<Scenario>;
  private _title: Array<Title>;
  private _colors : Array<Color>;
  private _resultConfig: ResultConfig;
  public configPath : string = `config/diagram.config.json`;
  private _selectedBuild : Build;
  constructor(private _jsonService: JsonService,private _colorService: ColorService, private _configservice: ConfigService) {
    this._selectionUpdate = new EventEmitter<SelectionUpdateEvent>();
    this._initEvent = new EventEmitter<null>();
    this._colorService.getColors(this.configPath).subscribe((colors : Color[]) => {
      this._colors = colors;
    });
    this._jsonService.getScenarios().subscribe((scenarios : Scenario[]) => {
      this._scenarios = scenarios
      this._initEvent.emit();
    });
    this._configservice.getResultConfig(this.configPath).subscribe((resultConfig: ResultConfig)=>{
      this._resultConfig = resultConfig;
    });
   }
 
  get Scenarios(){
    return this._scenarios;
  }

  get Build(){
    return this._selectedBuild;
  }

   public runScenario(scenario: Scenario): Observable<null>{
     return new Observable((observer) => {
      this._selectionUpdate.emit(new SelectionUpdateEvent("Clear",null));
      this._selectedBuild = this.getBuild(scenario.build);
      this._jsonService.getResults(this._selectedBuild.Name).subscribe((benchmarks : Benchmark[]) =>{
        this._benchmarks = benchmarks;
        this.createDiagramList(this._selectedBuild);
        this.setTitle(this._selectedBuild);
        scenario.diagrams.forEach((operationId: string)=> {
          let index = this._diagrams.findIndex((diagram: Diagram,index : number,diagrams: Diagram[]) =>{
            return diagram.title === this.resolveOperation(this._selectedBuild.ResultData,operationId).Title
          });
          this._selectionUpdate.emit(new SelectionUpdateEvent("Added",this._diagrams[index]));
          this._title[index].HasChecked = true;
        });

        observer.next();
        observer.complete();
      });
    });
  }

  public getBuild(buildId: String){
    return this._resultConfig.Build.find((build: Build)=>{
      return build.ID === buildId;
    });
  }


  get InitEvent(){
    return this._initEvent;
   }

   get SelectiponUpdateEvent(){
    return this._selectionUpdate;
   }


   updateSelection(added: boolean,title: string){
     let diagram = this._diagrams.find((diagram: Diagram)=>{
       return diagram.title == title;
     });
     let type = added == true ?  "Added" : "Removed";
     this._selectionUpdate.emit(new SelectionUpdateEvent(type,diagram));
   }

   private createDiagramList(build : Build){
    this._diagrams = new Array<Diagram>();
    this._benchmarks.forEach((benchmark: Benchmark)=>{
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
    let operation = this.resolveOperation(build.ResultData,benchmark.operationID);
    this._diagrams.push(new Diagram(operation.DiagramType,data,this.getOption(operation.YLabel,operation.XLabel),operation.Title))  
  });
   }

   private getDataSet(tool : Tool, index: number){
      let dataset: Dataset = new Dataset();
      dataset.lineTension = 0;
      dataset.data = new Array();
      dataset.fill = false;
      dataset.borderColor = this.getColor(this._colors[index].ToolName);
      dataset.backgroundColor = this.getColor(this._colors[index].ToolName);
      tool.results.forEach((result: Result) => {
        dataset.data.push((result.metric.MetricValue));
      });
      return dataset;
   }

    get Title(): Array<Title>{
      
      return this._title;
   }


   public getColor(toolName: String): string{
    return this._colors.find((color: Color)=>{
      return color.ToolName === toolName;
    }).Color;
   }

   private setTitle(build: Build){
    this._title = new Array<Title>();
    this._benchmarks.forEach((benchmark : Benchmark)=>{
     this._title.push(new Title(this.resolveOperation(build.ResultData,benchmark.operationID).Title,false))
    });
   }

   private resolveOperation(resultDatas: Array<ResultsData>,operationID: String){
     return resultDatas.find((resultData: ResultsData)=>{
       return resultData.OperationID === operationID;
     })
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
        display: false
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


export class Title{
  constructor(private _value: String, private _hasChecked: boolean){}

  set HasChecked(c : boolean){
    this._hasChecked = c;
  }

  get HasChecked(){
    return this._hasChecked;
  }

  get Value(){
    return this._value;
  }
}


export class SelectionUpdateEvent{
  constructor(private eventType: string, private diagram: Diagram){}
  get EventType(){
    return this.eventType;
  }

  get Diagram(){
    return this.diagram;
  }
}

