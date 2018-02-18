import { Component, OnInit } from '@angular/core';
import { Diagram } from '../model/diagram';
import { DiagramService } from '../service/diagram.service';

@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.css']
})
export class DiagramComponent implements OnInit {
  diagrams : Array<Diagram>;
  constructor(private _diagramService: DiagramService) {
    this._diagramService.Event.subscribe(() => {
        this.diagrams = this._diagramService.getDiagramDatas();
      })
   }

  ngOnInit() {
  }

}
