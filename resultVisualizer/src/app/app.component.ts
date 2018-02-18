import { Component , OnInit} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { DiagramService, Title } from './diagram/service/diagram.service';



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  
titles : Array<Title>
  constructor(private _diagramService: DiagramService){
    this.titles = new Array<Title>();
    this._diagramService.InitEvent.subscribe(() =>{
      this.titles = this._diagramService.Title;
    });
  }

  changedSelection(event: boolean,title: string){
    this._diagramService.updateSelection(event,title);
  }

  ngOnInit(){}

}
