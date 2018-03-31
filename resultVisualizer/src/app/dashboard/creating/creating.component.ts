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

  results : Array<BuildResult>;
  shows : Array<SelectionResult>;
  hides: Array<SelectionResult>;

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
          let diagList = new Array<Diagram>();
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
            diagList.push(new Diagram(key,value));
          })
          this._diagramService.addScenario(new Scenario(diagList,"custom"));
          this._router.navigate(['diagrams'], { queryParams: { 'scenario': 1, "type": "created" } });
        break;
      }
      this.step++;
    }
  }

   private onDropModel(args) {
    let [el, target, source] = args;
    this.disabled =  this.selects.length === 0;
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