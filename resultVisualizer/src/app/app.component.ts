import { Component , OnInit} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { DiagramService, Title } from './diagram/service/diagram.service';
import { Scenario } from './model/scenario';
import { DefaultScale } from './model/defaultScale';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] 
})

export class AppComponent implements OnInit{
  
titles : Array<string>
ngClass : Array<{
  "line-through": boolean
}>
scenarios : Array<Scenario>
selected : number;
started: boolean = false;
selectedSlider: DefaultScale;
scales: Array<DefaultScale>;
  constructor(private _diagramService: DiagramService){
    this.selectedSlider = new DefaultScale("Time",-9);
    this.titles = new Array<string>();
    this.ngClass = new  Array<{"line-through": boolean}>();
    this.scenarios = new Array<Scenario>();
    this._diagramService.InitEvent.subscribe((event) =>{
      switch(event){
        case "Scenario":
          this.scenarios = this._diagramService.Scenarios;
          this.selected = 0;
          break;
        case "Config":
          this.scales = this._diagramService.getScale();
          break;
      }
    });
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
      this._diagramService.Title.forEach((title) =>{
        this.ngClass.push(title.NgClass);
        this.titles.push(title.Value);
      });
  });
}

public changeSlider(slider: string){
  this.selectedSlider =  this.scales.find(scale =>{
    return scale.Metric === slider;
   });
}

  ngOnInit(){}


}
