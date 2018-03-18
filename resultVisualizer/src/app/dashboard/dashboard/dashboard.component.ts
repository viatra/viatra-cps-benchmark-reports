import { Component, OnInit } from '@angular/core';
import { Scenario } from '../../model/scenario';
import { DiagramService } from '../../diagram/service/diagram.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  state: string;
  constructor() {
    this.state = "start";

  }

  ngOnInit() {
  }


  public loadScenario(){
    this.state = "load";
  }
}
