import { Component , OnInit, OnChanges} from '@angular/core';
import { JsonService } from './services/json.service';
import { Benchmark } from './model/benchmark';
import { Tool } from './model/tool';
import { Result } from './model/result';
import { DiagramService, Title, TimeScale, MemoryScale } from './diagram/service/diagram.service';
import { Scenario } from './model/scenario';
import { Scale } from './model/defaultScale';
import { Router, UrlSegment, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] 
})


export class AppComponent implements OnInit{
  showSlider: boolean = false;
  constructor(private _router: Router){
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
  ngOnInit(){}
  
}
