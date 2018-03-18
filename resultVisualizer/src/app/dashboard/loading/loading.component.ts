import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Scenario } from '../../model/scenario';
import { DiagramService } from '../../diagram/service/diagram.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class LoadingComponent implements OnInit {
  scenarios : Array<Scenario>
  selected : number;
  @Output() back : EventEmitter<null>
  constructor(private _diagramService : DiagramService, private _router : Router) {
    this.back = new EventEmitter<null>()
    this._diagramService.InitEvent.subscribe((event)=>{
      if(event == "Scenario"){
        this.scenarios = this._diagramService.Scenarios;
        this.selected = 0;
      }
    })
    this.scenarios = this._diagramService.Scenarios;
    this.selected = 0;
   }

  ngOnInit() {
  }

  public clickedBack(){
    this.back.emit();
  }


  public selectionChange(scenario :string){
    this.selected = this.scenarios.findIndex((sc: Scenario, index: number,scenarios : Scenario[]) =>{
      return sc.name == scenario;
    })
  }

  public select(){
    this._router.navigate(['diagrams'], { queryParams: { 'scenario': this.selected, "type": "loaded" } });
  }
}
