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
        this.diagrams = new Array<Diagram>();
        let diagram = new Diagram();
        diagram.data = this._diagramService.getDiagramData("Init M2M");
        diagram.options = this._diagramService.getOption("size","time");
        diagram.type = "line";
        this.diagrams.push(diagram)
        console.log(diagram)
      })
   }

  ngOnInit() {
  }

}
