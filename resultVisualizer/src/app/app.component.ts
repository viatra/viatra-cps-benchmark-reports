import { Component , OnInit, OnChanges} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { DiagramService, Title, Glyphicon } from './diagram/service/diagram.service';
import { DiagramSet } from './model/diagramSet';
import { Scale } from './model/defaultScale';
import { Router, UrlSegment, NavigationEnd } from '@angular/router';
import { ComponentService } from './component.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] 
})


export class AppComponent implements OnInit{
  title : {
    diagramTitle : Glyphicon,
    legendTitle: Glyphicon
  }

  
  showSlider: boolean = false;
  constructor(private _router: Router, private _componentService: ComponentService){
    this.title = {
      "diagramTitle" : {
      "glyphicon" : true,
      "glyphicon-eye-open": true,
      "glyphicon-eye-close": false,
      "missing": false
    },
      "legendTitle":  {
      "glyphicon" : true,
      "glyphicon-eye-open": true,
      "glyphicon-eye-close": false,   
      "missing": false
    }
  }
    this._router.events.subscribe(event=>{
      if(event instanceof  NavigationEnd){
        if((event as NavigationEnd).url.includes("diagrams")){
          this.showSlider = true;
        }else{
          this.showSlider = false;
        }
      }
    });
  }


  select(component: string){
        this.title[component]["glyphicon-eye-open"] = !this.title[component]["glyphicon-eye-open"];
        this.title[component]["glyphicon-eye-close"] = ! this.title[component]["glyphicon-eye-close"];
      this._componentService.updateComponent(component,this.title[component]["glyphicon-eye-close"]);
  } 
  ngOnInit(){}
  
}
