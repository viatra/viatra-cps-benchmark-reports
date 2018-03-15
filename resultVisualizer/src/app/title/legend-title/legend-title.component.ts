import { Component, OnInit } from '@angular/core';
import { DiagramService, SelectionUpdateEvent } from '../../diagram/service/diagram.service';
import { Legend } from '../../model/legend';
import { Data } from '../../diagram/model/data';
import { tick } from '@angular/core/testing';

@Component({
  selector: 'app-legend-title',
  templateUrl: './legend-title.component.html',
  styleUrls: ['./legend-title.component.css']
})
export class LegendTitleComponent implements OnInit {
  legends : Array<Legend>
  checked: Array<{"line-through": boolean}>

  constructor(private _diagramService: DiagramService) {
    this.legends = new Array<Legend>();
    this.checked = new Array<{"line-through": boolean}>();

    this._diagramService.SelectiponUpdateEvent.subscribe((selectionUpdateEvent: SelectionUpdateEvent) =>{
      if(selectionUpdateEvent.EventType == "Added"){
        selectionUpdateEvent.Diagram.data.datasets.forEach((dataset)=>{
          let title = this.legends.find((legend)=>{
            return legend.Title === dataset.label;
          });
          if(title === null || title === undefined){
            this.legends.push(new Legend(dataset.label,dataset.backgroundColor,1));
            this.checked.push({"line-through": false});
          } else{
            title.Count++;
          }
        });
      } else if(selectionUpdateEvent.EventType == "Removed"){
        selectionUpdateEvent.Diagram.data.datasets.forEach((dataset)=>{
          let title = this.legends.find((legend)=>{
            return legend.Title === dataset.label;
          });
          if(title !== null && title !== undefined){
            title.Count--;
            if(title.Count == 0){
              this.checked.splice(this.legends.indexOf(title),1)
              this.legends.splice(this.legends.indexOf(title),1);
            }
          }
        });
      }else if(selectionUpdateEvent.EventType == "Clear"){
      this.legends = new Array<Legend>();
      }
    });
   }

   changedSelection(index: number){
    this.checked[index]["line-through"] = !this.checked[index]["line-through"];
    this._diagramService.updateLegend(this.checked[index]["line-through"],this.legends[index].Title);

  }

  ngOnInit() {
  }

}
