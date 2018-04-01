import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { ResultsData } from '../../model/resultData';
import { DragulaService } from 'ng2-dragula';
import { Benchmark } from '../../model/benchmark';
import { DiagramService } from '../../diagram/service/diagram.service';
import { Scenario, Diagram, Result } from '../../model/scenario';
import { Router } from '@angular/router'

@Component({
  selector: 'app-creating',
  templateUrl: './creating.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class CreatingComponent implements OnInit {
  @Output() back : EventEmitter<null>
  next = {
    "glyphicon" :true,
    "glyphicon-arrow-right" : true,
    "disabled" : true
  }
  disabled : boolean;
  builds : Array<string>
  selects: Array<string>;
  step : number;
  scenarioTitle: String;
  results : Array<BuildResult>;
  shows : Array<SelectionResult>;
  hides: Array<SelectionResult>;
  private _diagrams: Array<Diagram>;
  constructor(private _jsonService: JsonService, private _dragulaService: DragulaService,private _diagramService: DiagramService, private _router: Router) { 
    this.back = new EventEmitter<null>();
    this.builds = new Array<string>();
    this.selects = new Array<string>();

    this.results =  new Array<BuildResult>();
    this.shows =  new Array<SelectionResult>();
    this.hides =  new Array<SelectionResult>();

    this.disabled = true;
    this.step = 1;
    const bag: any = this._dragulaService.find('result');
    if (bag !== undefined ) this._dragulaService.destroy('result');
    this._dragulaService.setOptions('result', {
      accepts: (el, target, source, sibling) => {
        return target.id === ""
      },
      revertOnSpill: true 
      });

    this._dragulaService.dropModel.subscribe((value) => {
      this.onDropModel(value.slice(1));
    });
  }

  public clickedBack(){
    this.back.emit();
    this.disabled = false;
    this.next["disabled"] = this.disabled;
    this.step--;
  }


  public select(){
    if(!this.disabled){
      switch(this.step){
        case 1: 
        this.results = new Array<BuildResult>();
        this.shows =  new Array<SelectionResult>();
        this.hides =  new Array<SelectionResult>();
        this.selects.forEach(select=>{
          this._jsonService.getResults(select).subscribe((res: Array<Benchmark>)=>{
            let result = new Array<SelectionResult>();
         
            res.forEach(element => {
              let build = this._diagramService.getResultData(select);
              let operation = this._diagramService.resolveOperation(build.ResultData,element.operationID);
              result.push(new SelectionResult(build.ID,operation.OperationID,`${operation.Title} (${build.ID})`));
            });
            this.results.push(new BuildResult(select,result));
          })
        })
        break;
        case 2:
          this._diagrams = new Array<Diagram>();
          let hideMap = new Map<String,Result>()
          this.hides.forEach(hide=>{
            hideMap.set(hide.BuildID,new Result(new Array<String>(),new Array<String>()));
          })
          this.shows.forEach(hide=>{
            hideMap.set(hide.BuildID,new Result(new Array<String>(),new Array<String>()));
          })

          hideMap.forEach((value,key)=>{
            
            this.hides.forEach((hide)=>{
              if(hide.BuildID === key){
                value.closed.push(hide.OperationID)
              }
            })
            this.shows.forEach((show)=>{
              if(show.BuildID === key){
                value.opened.push(show.OperationID)
              }
            })
            this._diagrams.push(new Diagram(key,value));
          })
        break;

        case 3: 
        this._diagramService.addScenario(new Scenario(this._diagrams,this.scenarioTitle));
        this._router.navigate(['diagrams'], { queryParams: { 'scenario': 1, "type": "created" } });
        break;
      }
      this.disabled = true;
      this.next["disabled"] = this.disabled;
      this.step++;
    }
  }

   private onDropModel(args) {
    let [el, target, source] = args;
    switch(this.step){
      case 1:
      this.disabled =  this.selects.length === 0;
      break;
      case 2:
      this.disabled = this.shows.length === 0 && this.hides.length === 0;
      break;
      case 3:
      this.disabled = this.scenarioTitle === "";
      break;
    }
    this.next["disabled"] = this.disabled;

  }

  public updateScenarioTitle(){
    this.disabled = this.scenarioTitle === "";
    this.next["disabled"] = this.disabled;
  }





  ngOnInit() {
    this._jsonService.getBuilds().subscribe((builds : Array<string>)=>{
      this.builds = builds;
    });
  }
}

class BuildResult{
  constructor(private _buildName: string, private _results : Array<SelectionResult>){}

  get BuildName(){
    return this._buildName;
  }

  get Results(){
    return this._results;
  }
}


class SelectionResult{
  constructor(private _buildID: String, private _operationID : String, private _resultName){}

  get BuildID(){
    return this._buildID;
  }

  get OperationID(){
    return this._operationID;
  }

  get ResultName(){
    return this._resultName;
  }
}