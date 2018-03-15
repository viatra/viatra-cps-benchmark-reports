import { Component, OnInit, Input,  } from '@angular/core';
import { Title, DiagramService } from '../../diagram/service/diagram.service';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-diagram-title',
  templateUrl: './diagram-title.component.html',
  styleUrls: ['./diagram-title.component.css']
})
export class DiagramTitleComponent implements OnInit {
  @Input() titles: Array<string>
  @Input() checked: Array<{
    "line-through": boolean
  }>
  constructor(private _diagramService: DiagramService) {
   }

   changedSelection(index: number){
    this._diagramService.updateSelection(this.checked[index]["line-through"],this.titles[index]);
    this.checked[index][ "line-through"] = !this.checked[index][ "line-through"];
  }

  ngOnInit() {
  }


}
