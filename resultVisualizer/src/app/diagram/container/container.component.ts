import { Component, OnInit, Input } from '@angular/core';
import { DiagramService } from '../service/diagram.service';
import { ActivatedRoute } from '@angular/router/';
import { Scenario } from '../../model/scenario';
import { Scale } from '../../model/defaultScale';
import { SliderService } from '../../slider/slider.service';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {

  public scale: Scale
  public titles : Array<string>
  public ngClass : Array<{
  "line-through": boolean
}>
  constructor(private _diagramService: DiagramService, private _route : ActivatedRoute, private _sliderService : SliderService) {}

  ngOnInit() {
    this.titles = new Array<string>();
    this.ngClass = new  Array<{"line-through": boolean}>();
    let scenario =  this._route.snapshot.queryParams['scenario'];
    let type = this._route.snapshot.queryParams['type'];
   this._diagramService.runScenario(scenario,type).subscribe((initialized)=>{
     if(initialized){
      this._diagramService.Title.forEach((title) =>{
        this.ngClass.push(title.NgClass);
        this.titles.push(title.Value);
        });
      }
   });
   this._sliderService.ScaleChangeEvent.subscribe(scale =>{
     this.scale = scale;
   })

  }


  }
