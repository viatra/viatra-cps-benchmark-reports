import { Component, OnInit , ViewChildren, QueryList} from '@angular/core';

import { Diagram } from '../model/diagram';
import { ChartComponent } from 'angular2-chartjs';
import { DiagramService, SelectionUpdateEvent } from '../service/diagram.service';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.css']
})
export class DiagramComponent implements OnInit {
  @ViewChildren(ChartComponent) chart: QueryList<ChartComponent>; 
  diagrams : Array<Diagram>;
  ngClass: any;
  constructor(private _diagramService: DiagramService) {
    this.diagrams = new Array<Diagram>();
    this._diagramService.SelectiponUpdateEvent.subscribe((selectionUpdateEvent: SelectionUpdateEvent) =>{
      if(selectionUpdateEvent.EventType == "Added"){
        this.diagrams.push(selectionUpdateEvent.Diagram);
        this.updateClass();
      }else if(selectionUpdateEvent.EventType == "Removed"){
        this.diagrams.splice(this.diagrams.indexOf(selectionUpdateEvent.Diagram),1);
        this.updateClass();
      }else if(selectionUpdateEvent.EventType == "Clear"){
        this.diagrams = new Array<Diagram>();
        this.updateClass();
      }
    });
  }

  hide(){
    console.log(this.chart)
    this.diagrams.forEach(diagram =>{
      diagram.data.datasets[0].hidden = true;
    })
    this.chart.forEach(c =>{
      c.chart.update();
    })
  }

  updateClass(){
    this.ngClass= {
      "col" : true,
      "col-lg-12" : true
    }
  }
  ngOnInit() {
  }

}
