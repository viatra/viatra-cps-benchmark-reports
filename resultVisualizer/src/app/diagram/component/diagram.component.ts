import { Component, OnInit , ViewChildren, QueryList} from '@angular/core';

import { Diagram } from '../model/diagram';
import { ChartComponent } from 'angular2-chartjs';
import { DiagramService, SelectionUpdateEvent, LegendUpdateEvent } from '../service/diagram.service';

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
    this.updateLegend();
  }

  updateLegend(){
    this._diagramService.LegendUpdateEvent.subscribe((event : LegendUpdateEvent) =>{
      this.diagrams.forEach(diagram =>{
        let dataset = diagram.data.datasets.find((dataset)=>{
          return dataset.label === event.ToolName;
        });
        if(dataset !== null && dataset != undefined){
          dataset.hidden = event.EventType === "hide" ? true : false;
        }
      });
      this.chart.forEach(c =>{
        c.chart.update();
      })
    });
    
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
