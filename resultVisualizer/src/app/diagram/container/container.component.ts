import { Component, OnInit, Input } from '@angular/core';
import { DiagramService, Glyphicon } from '../service/diagram.service';
import { ActivatedRoute } from '@angular/router/';
import { Scenario } from '../../model/scenario';
import { Scale } from '../../model/defaultScale';
import { SliderService } from '../../slider/slider.service';
import { Diagram } from '../model/diagram';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {

  public scale: Scale
  public titles : Array<DiagramLabel>
  constructor(private _diagramService: DiagramService, private _route : ActivatedRoute, private _sliderService : SliderService) {
    this.scale = new Scale("Time",-9,-9,"ns");
  }

  ngOnInit() {
    this.titles = new Array<DiagramLabel>();
    let scenario =  this._route.snapshot.queryParams['scenario'];
    let type = this._route.snapshot.queryParams['type'];
   this._diagramService.runScenario(scenario,type).subscribe((initialized)=>{
     if(initialized){
      this._diagramService.Title.forEach((title) =>{
        this.titles.push(new DiagramLabel(title.Value,title.NgClass,title.ID));
        });
        this.titles.sort( (a,b) => {
          if(a.ID < b.ID) return -1;
          if(a.ID > b.ID) return 1;
          return 0;
        })
      }
   });
   this._sliderService.ScaleChangeEvent.subscribe(scale =>{
     this.scale = scale;
   })

  }


  }

  export class DiagramLabel{
    constructor(public title :string,public ngClass : Glyphicon, public ID: String){}
  }
