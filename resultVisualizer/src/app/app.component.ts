import { Component , OnInit} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { DiagramService, Title } from './diagram/service/diagram.service';
import { Scenario } from './model/scenario';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  
titles : Array<Title>
scenarios : Array<Scenario>
selected : number;
started: boolean = false;
  constructor(private _diagramService: DiagramService){
    this.titles = new Array<Title>();
    this.scenarios = new Array<Scenario>();
    this._diagramService.InitEvent.subscribe(() =>{
      this.scenarios = this._diagramService.Scenarios;
      this.selected = 0;
    });
  }

  changedSelection(event: boolean,title: string){
    this._diagramService.updateSelection(event,title);
  }

  public home(){
    this.started = false;
  }

  public selectionChange(scenario :string){
    this.selected = this.scenarios.findIndex((sc: Scenario, index: number,scenarios : Scenario[]) =>{
      return sc.name == scenario;
    })
  }

  public select(){
    this.started = true;
    this._diagramService.runScenario(this.scenarios[this.selected]).subscribe(()=>{
      this.titles = this._diagramService.Title;
    });
 
  }

  ngOnInit(){}

}
