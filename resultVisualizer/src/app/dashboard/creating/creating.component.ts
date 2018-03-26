import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { build$ } from 'protractor/built/element';
import { ResultsData } from '../../model/resultData';
import { DragulaService } from 'ng2-dragula';


@Component({
  selector: 'app-creating',
  templateUrl: './creating.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class CreatingComponent implements OnInit {
  @Output() back : EventEmitter<null>
  next = {
    "glyphicon" :true,
    "glyphicon-arrow-right" : true,
    "disabled" : true
  }
  disabled : boolean;
  builds : Array<string>
  selects: Array<string>;
  constructor(private _jsonService: JsonService, private _dragulaService: DragulaService) { 
    this.back = new EventEmitter<null>();
    this.builds = new Array<string>();
    this.selects = new Array<string>();
    this.disabled = true;
    this._dragulaService.dropModel.subscribe((value) => {
      this.onDropModel(value.slice(1));
    });
    this._dragulaService.removeModel.subscribe((value) => {
      this.onRemoveModel(value.slice(1));
    });
  }

  public clickedBack(){
    this.back.emit();
  }


  public select(){
    console.log(this.disabled);
  }

   private onDropModel(args) {
    let [el, target, source] = args;
    this.disabled =  this.selects.length === 0;
    this.next["disabled"] = this.disabled;
  }

  private onRemoveModel(args) {
    let [el, source] = args;
  }



  ngOnInit() {
    this._jsonService.getBuilds().subscribe((builds : Array<string>)=>{
      this.builds = builds;
    });
  }
}
