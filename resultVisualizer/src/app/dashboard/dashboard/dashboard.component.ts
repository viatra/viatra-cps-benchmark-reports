import { Component, OnInit } from '@angular/core';
import { DiagramSet } from '../../model/diagramSet';
import { DiagramService } from '../../diagram/service/diagram.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor() {}
  ngOnInit() {}  
}
