import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { build$ } from 'protractor/built/element';
import { ResultsData } from '../../model/resultData';


@Component({
  selector: 'app-creating',
  templateUrl: './creating.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class CreatingComponent implements OnInit {
  @Output() back : EventEmitter<null>

  builds : Array<string>
  diagrams: Array<string>;
  constructor(private _jsonService: JsonService) { 
    this.back = new EventEmitter<null>()
    this.builds = new Array<string>();
    this.diagrams = new Array<string>();
  }

  public clickedBack(){
    this.back.emit();
  }



  ngOnInit() {
    this._jsonService.getBuilds().subscribe((builds : Array<string>)=>{
      this.builds = builds;
    });
  }

  public change(event: Event,index :number){
      this._jsonService.getResults(this.builds[index]).subscribe((results: ResultsData[]) =>{
        results.forEach(result =>{
          console.log(result)
          if((event.srcElement as HTMLInputElement).checked == true){
          this.diagrams.push(`${this.builds[index]}: ${result.OperationID}`);
          } else{
            this.diagrams.splice(this.diagrams.indexOf(`${this.builds[index]}: ${result.OperationID}`),1);
          }
        })
      });
  }

}
