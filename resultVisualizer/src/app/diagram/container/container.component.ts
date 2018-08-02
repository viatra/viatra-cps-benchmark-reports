import { Component, OnInit, Input } from '@angular/core';
import { DiagramService, Glyphicon } from '../service/diagram.service';
import { ActivatedRoute } from '@angular/router';
import { DiagramSet } from '../../model/diagramSet';
import { Scale } from '../../model/defaultScale';
import { SliderService } from '../../slider/slider.service';
import { Diagram } from '../model/diagram';
import { ComponentService, ComponentUpdateEvent } from '../../component.service';

@Component({
  selector: 'app-container',
  templateUrl: './container.component.html',
  styleUrls: ['./container.component.css']
})
export class ContainerComponent implements OnInit {
  public scale: Scale
  center: any;
  hidden = {
    diagramTitle: false,
    legendTitle: false
  }
  private col7 = {
    "col": true,
    "col-lg-7": true
  }
  private col9 = {
    "col": true,
    "col-lg-9": true
  }

  private col10 = {
    "col": true,
    "col-lg-10": true
  }
  private col12 = {
    "col": true,
    "col-lg-12": true
  }

  public titles: Array<DiagramLabel>
  constructor(private _diagramService: DiagramService, private _route: ActivatedRoute, private _sliderService: SliderService, private _componentservice: ComponentService) {
    this.hidden = {
      "diagramTitle": false,
      "legendTitle": false
    }
    this.scale = null;
    this.center = this.col7;
    this._componentservice.ComponentUpdate.subscribe((event: ComponentUpdateEvent) => {
      this.hidden[event.component] = event.hide;
      if (this.hidden.diagramTitle === true && this.hidden.legendTitle === true) {
        this.center = this.col12;
      }
      else if (this.hidden.diagramTitle === true) {
        this.center = this.col9;
      } else if (this.hidden.legendTitle === true) {
        this.center = this.col10;
      } else {
        this.center = this.col7;
      }
    })
  }

  ngOnInit() {
    this.titles = new Array<DiagramLabel>();
    let scenario = this._route.snapshot.queryParams['scenario'];
    let type = this._route.snapshot.queryParams['type'];
    this._diagramService.runScenario(scenario, type, () => {
      this.titles = new Array<DiagramLabel>();
      this._diagramService.Title.forEach((title) => {

        this.titles.push(new DiagramLabel(title.Value, title.NgClass, title.ID));
      });
      this.titles.sort((a, b) => {
        if (a.ID < b.ID) return -1;
        if (a.ID > b.ID) return 1;
        return 0;
      })
    });
  }
}

export class DiagramLabel {
  constructor(public title: string, public ngClass: Glyphicon, public ID: String) { }
}
