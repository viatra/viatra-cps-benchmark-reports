import { Component, OnInit } from '@angular/core';
import { Scenario } from '../../model/scenario';
import { DiagramService } from '../../diagram/service/diagram.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  state: string;
  scenarios : Array<Scenario>
  selected : number;
  constructor(private _diagramService : DiagramService) {
    this.state = "start";
    this.scenarios = new Array<Scenario>();
  }

  ngOnInit() {
  }
  loadScenario(){
    this.state = "load";
    this.scenarios = this._diagramService.Scenarios;
    this.selected = 0;
  }


  public selectionChange(scenario :string){
    this.selected = this.scenarios.findIndex((sc: Scenario, index: number,scenarios : Scenario[]) =>{
      return sc.name == scenario;
    })
  }
}
