import { Component, OnInit, Input,  } from '@angular/core';
import { Title, DiagramService, Glyphicon } from '../../diagram/service/diagram.service';
import { Observable } from 'rxjs/Observable';
import { DragulaService } from 'ng2-dragula';
import { DiagramLabel } from '../../diagram/container/container.component';

@Component({
  selector: 'app-diagram-title',
  templateUrl: './diagram-title.component.html',
  styleUrls: ['./diagram-title.component.css']
})
export class DiagramTitleComponent implements OnInit {
  @Input() titles: Array<DiagramLabel>
  constructor(private _diagramService: DiagramService, private _dragulaService: DragulaService) {
    this._dragulaService.dropModel.subscribe((value) => {
      this.onDropModel(value.slice(1));
    });
    this._dragulaService.removeModel.subscribe((value) => {
      this.onRemoveModel(value.slice(1));
    });
   }

   private onDropModel(args) {
    let [el, target, source] = args;
    this._diagramService.sortDiagrams(this.titles);
  }

  private onRemoveModel(args) {
    let [el, source] = args;
    // do something else
  }

   changedSelection(index: number){
    this._diagramService.updateSelection(this.titles[index].ngClass["glyphicon-eye-close"],this.titles[index].title);
    this.titles[index].ngClass[ "glyphicon-eye-close"] = !this.titles[index].ngClass[ "glyphicon-eye-close"];
    this.titles[index].ngClass[ "glyphicon-eye-open"] = !this.titles[index].ngClass[ "glyphicon-eye-open"];
    this._diagramService.sortDiagrams(this.titles);
  }

  ngOnInit() {
  }


}
