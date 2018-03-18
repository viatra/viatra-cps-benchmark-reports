import { Component, OnInit, Output, EventEmitter } from '@angular/core';


@Component({
  selector: 'app-creating',
  templateUrl: './creating.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class CreatingComponent implements OnInit {
  @Output() back : EventEmitter<null>
  constructor() { 
    this.back = new EventEmitter<null>()
  }

  public clickedBack(){
    this.back.emit();
  }



  ngOnInit() {
  }

}
