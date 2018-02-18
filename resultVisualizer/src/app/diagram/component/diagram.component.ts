import { Component, OnInit } from '@angular/core';
import { Diagram } from '../model/diagram';
import { DiagramService, SelectionUpdateEvent } from '../service/diagram.service';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.css']
})
export class DiagramComponent implements OnInit {
  diagrams : Array<Diagram>;
  constructor(private _diagramService: DiagramService) {
    this.diagrams = new Array<Diagram>();
    this._diagramService.SelectiponUpdateEvent.subscribe((selectionUpdateEvent: SelectionUpdateEvent) =>{
      if(selectionUpdateEvent.EventType == "Added"){
        this.diagrams.push(selectionUpdateEvent.Diagram);
      }else if(selectionUpdateEvent.EventType == "Removed"){
        this.diagrams.splice(this.diagrams.indexOf(selectionUpdateEvent.Diagram),1);
      }
    });
  }
  ngOnInit() {
  }
}
