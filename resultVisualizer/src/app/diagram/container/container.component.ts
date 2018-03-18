import { Component, OnInit, Input } from '@angular/core';
import { DiagramService } from '../service/diagram.service';
import { ActivatedRoute } from '@angular/router/';
import { Scenario } from '../../model/scenario';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {
  @Input() scale: number;
  @Input() metric: string;
  @Input() default: number;
  
  public titles : Array<string>
  public ngClass : Array<{
  "line-through": boolean
}>
  constructor(private _diagramService: DiagramService, private _route : ActivatedRoute) {}

  ngOnInit() {
    this.titles = new Array<string>();
    console.log(this._route.snapshot.queryParams['scenario'])
    this.ngClass = new  Array<{"line-through": boolean}>();
    let scenario =  this._route.snapshot.queryParams['scenario'];
    let type = this._route.snapshot.queryParams['type'];
   this._diagramService.runScenario(scenario,type).subscribe((initialized)=>{
     if(initialized){
      console.log(this._diagramService.Title);
      this._diagramService.Title.forEach((title) =>{
        
        this.ngClass.push(title.NgClass);
        this.titles.push(title.Value);
        });
      }
   });
  }


  }
