import { Component, OnInit, ViewChildren, QueryList, Input, OnChanges } from '@angular/core';

import { Diagram } from '../model/diagram';
import { ChartComponent } from 'angular2-chartjs';
import { DiagramService, SelectionUpdateEvent, LegendUpdateEvent } from '../service/diagram.service';
import { Scale } from '../../model/defaultScale';
import { MatDialog } from '@angular/material';
import { ComponentService, ClassUpdateEvent } from '../../component.service';
import { SliderService } from '../../slider/slider.service';

import * as cloneDeep from 'lodash/cloneDeep';
@Component({
  selector: 'app-diagram',
  templateUrl: './diagram.component.html',
  styleUrls: ['./diagram.component.css']
})
export class DiagramComponent implements OnInit {
  @ViewChildren(ChartComponent) chart: QueryList<ChartComponent>;
  ngClass: any;
  parent: string;


  private _ngClass = {
    "col12": {
      "col": true,
      "col-lg-12": true
    },
    "col6": {
      "col": true,
      "col-lg-6": true
    },
    "col4": {
      "col": true,
      "col-lg-4": true
    },
    "line": {
      "inline": true
    }
  }



  diagrams: Array<Diagram>;
  private _scale: Scale;
  constructor(private _diagramService: DiagramService,
    private _sliderService: SliderService,
    public _dialog: MatDialog,
    private _componentService: ComponentService) {
    this.ngClass = this._ngClass["col12"];
    this.parent = "row"
    this._componentService.ClassUpdate.subscribe((event: ClassUpdateEvent) => {
      this.ngClass = this._ngClass[event.ngClass];
      if (event.ngClass == "line") {
        this.parent = "line";
      } else {
        this.parent = "row"
      }
    })
    this.diagrams = new Array<Diagram>();
    this._diagramService.SelectiponUpdateEvent.subscribe((selectionUpdateEvent: SelectionUpdateEvent) => {
      if (selectionUpdateEvent.EventType == "Added") {
        let diagram = cloneDeep(selectionUpdateEvent.Diagram)
        if (this._scale != null) {
          this.setScale(this._scale, diagram);
        }
        this.diagrams.push(diagram);
      } else if (selectionUpdateEvent.EventType == "Removed") {
        this.diagrams.splice(this.diagrams.indexOf(selectionUpdateEvent.Diagram), 1);
      } else if (selectionUpdateEvent.EventType == "Clear") {
        this.diagrams = new Array<Diagram>();
      }
    });
    this.updateLegend();

  }

  openDialog() {
    const dialogRef = this._dialog.open(GridSelectionDialog, {
      height: '200px'
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

  updateLegend() {
    this._diagramService.LegendUpdateEvent.subscribe((event: LegendUpdateEvent) => {
      this.diagrams.forEach(diagram => {
        let dataset = diagram.data.datasets.find((dataset) => {
          return dataset.label === event.ToolName;
        });
        if (dataset !== null && dataset != undefined) {
          dataset.hidden = event.EventType === "hide" ? true : false;
        }
      });
      this.chart.forEach(c => {
        c.chart.update();
      })
    });

  }

  ngOnInit() {
    this._sliderService.ScaleChangeEvent.subscribe((scale: Scale) => {
      this._scale = scale;
      if (scale.UnitIndex != scale.PrevIndex) {
        this.changeScale(scale)
      }
    })
  }

  setScale(scale: Scale, diagram: Diagram) {
    if (diagram.metric === scale.Metric) {
      diagram.data.datasets.forEach(dataset => {
        let defaultIndex = scale.Units.findIndex(unit => unit.Label === scale.DefaultScale)
        let datas = new Array<{ x: Number, y: number }>();
        dataset.data.forEach(data => {
          if (scale.UnitIndex > defaultIndex) {
            for (let i = defaultIndex; i < scale.UnitIndex; i++) {
              data.y = data.y / scale.Units[i].Value[1]
            }
          } else {
            for (let i = defaultIndex; i > scale.UnitIndex; i--) {
              data.y = data.y * scale.Units[i].Value[0]
            }
          }
          datas.push({ x: data.x, y: data.y })
        });
        dataset.data = datas;
      })
    }
  }

  changeScale(scale: Scale) {
    if (this.chart != null && this.chart != undefined) {
      this.diagrams.forEach(diagram => {
        if (diagram.metric === scale.Metric) {
          diagram.data.datasets.forEach(dataset => {

            let datas = new Array<{ x: Number, y: number }>();
            dataset.data.forEach(data => {
              if (scale.UnitIndex > scale.PrevIndex) {
                for (let i = scale.PrevIndex; i < scale.UnitIndex; i++) {
                  data.y = data.y / scale.Units[i].Value[1]
                }
              } else {
                for (let i = scale.PrevIndex; i > scale.UnitIndex; i--) {
                  data.y = data.y * scale.Units[i].Value[0]
                }
              }
              datas.push({ x: data.x, y: data.y })
            });
            dataset.data = datas;
          })
        }
      })
      this.chart.forEach(c => {
        c.chart.update();
      })
    }

  }
}

@Component({
  selector: 'grid-selection-dialog',
  templateUrl: 'grid-selection-dialog.html',
  styleUrls: ['./diagram.component.css']
})
export class GridSelectionDialog {

  constructor(private _componentService: ComponentService) { }
  selectedGrid: string;

  grids = [
    'line',
    'row',
    'two',
    'three',
  ];

  selectGrid() {
    console.log(this.selectedGrid);
    let ngClass = "";
    switch (this.selectedGrid) {
      case "row":
        ngClass = "col12";
        break;
      case "two":
        ngClass = "col6";
        break;
      case "three":
        ngClass = "col4";
        break;
      case "line":
        ngClass = "line";
        break;
    }
    this._componentService.updateClass(ngClass);
  }
}