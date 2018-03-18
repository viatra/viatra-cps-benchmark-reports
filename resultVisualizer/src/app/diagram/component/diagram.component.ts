import { Component, OnInit , ViewChildren, QueryList, Input, OnChanges} from '@angular/core';

import { Diagram } from '../model/diagram';
import { ChartComponent } from 'angular2-chartjs';
import { DiagramService, SelectionUpdateEvent, LegendUpdateEvent } from '../service/diagram.service';
import { Scale } from '../../model/defaultScale';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.css']
})
export class DiagramComponent implements OnInit, OnChanges {
  @ViewChildren(ChartComponent) chart: QueryList<ChartComponent>; 
  @Input() scale: number;
  @Input() metric: string;
  @Input() default: number;
  private _prev;
  private _prevMetric;
  diagrams : Array<Diagram>;
  ngClass: any;
  constructor(private _diagramService: DiagramService) {
    this._prev = -9;
    this.diagrams = new Array<Diagram>();
    this._diagramService.SelectiponUpdateEvent.subscribe((selectionUpdateEvent: SelectionUpdateEvent) =>{
      if(selectionUpdateEvent.EventType == "Added"){
        let diagram = selectionUpdateEvent.Diagram
        if(diagram.scale === null){
          console.log("null " + diagram.scale)
        }if(selectionUpdateEvent.Diagram.scale === undefined){
          console.log("undefined " + diagram.scale)
          console.log(diagram)
          this.changeScale(this.default,diagram);
        }else{
          console.log(diagram.scale)
          this.changeScale(diagram.scale,diagram);
        }
        this.diagrams.push(diagram);
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

  ngOnChanges(){
    if(this.metric == this._prevMetric){
      
      this.diagrams.forEach(diagram =>{
        this.changeScale(this._prev,diagram);
      });
    }
    this._prevMetric = this.metric;
    this._prev = this.scale;
    
  }
  changeScale(prev: number,diagram: Diagram){
      if(this.chart != null && this.chart != undefined){
        if(diagram.metric === this.metric){
          diagram.scale = this.scale;
          diagram.data.datasets.forEach(dataset=>{
            let change = prev - this.scale;
            console.log(change);
            if(change != 0){
              let datas = new Array<number>();
              dataset.data.forEach(data=>{
                switch(diagram.metric){
                   case "Time":
                    datas.push(data * (Math.pow(10,change)));
                  break;
                  case "Memory":
                    datas.push(data * (Math.pow(Math.pow(2,10),change)))
                  break;
                }
              });
            dataset.data = datas;
            }
          });
        }
      this.chart.forEach(c =>{
        c.chart.update();
      })
    }
  
}
  }
