import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { build$ } from 'protractor/built/element';
import { ResultsData } from '../../model/resultData';
import { DragulaService } from 'ng2-dragula';
import { Result } from '../../model/result';
import { Benchmark } from '../../model/benchmark';
import { DiagramService } from '../../diagram/service/diagram.service';


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
  shows : Array<String>;
  hides: Array<String>;

  constructor(private _jsonService: JsonService, private _dragulaService: DragulaService,private _diagramService: DiagramService) { 
    this.back = new EventEmitter<null>();
    this.builds = new Array<string>();
    this.selects = new Array<string>();

    this.results =  new Array<BuildResult>();
    this.shows =  new Array<string>();
    this.hides =  new Array<string>();

    this.disabled = true;
    this.step = 1;

    this._dragulaService.setOptions('result', {
      accepts: (el, target, source, sibling) => {
        return target.id === ""
      }
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
        this.shows =  new Array<string>();
        this.hides =  new Array<string>();
        this.selects.forEach(select=>{
          this._jsonService.getResults(select).subscribe((res: Array<Benchmark>)=>{
            let result = new Array<string>();
         
            res.forEach(element => {
              let build = this._diagramService.getResultData(select);
              let operation = this._diagramService.resolveOperation(build.ResultData,element.operationID);
              result.push(`${operation.Title} (${build.ID})`);
            });
            this.results.push(new BuildResult(select,result));
          })
        })
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
  constructor(private _buildName: string, private _results : Array<String>){}

  get BuildName(){
    return this._buildName;
  }

  get Results(){
    return this._results;
  }
}
