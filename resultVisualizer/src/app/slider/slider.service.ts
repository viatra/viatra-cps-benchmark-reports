import { Injectable, EventEmitter } from '@angular/core';
import { DiagramService } from '../diagram/service/diagram.service';
import { Scale } from '../model/defaultScale';
import { ScaleMetric } from '../model/scaleMetric';

import * as cloneDeep from 'lodash/cloneDeep';

@Injectable()
export class SliderService {

  private _scales: Array<Scale>
  private _scaleChangeEvent: EventEmitter<Scale>
  private _initEvent: EventEmitter<null>
  private _resetEvent: EventEmitter<null>
  private _metrics: Array<ScaleMetric>
  constructor(private _diagramService: DiagramService) {
    this._initEvent = new EventEmitter<null>();
    this._resetEvent = new EventEmitter<null>();
    this._scaleChangeEvent = new EventEmitter<Scale>();
    this._metrics = Array<ScaleMetric>();
    this._scales = this._diagramService.getScale();
    if (this._scales === null || this._scales === undefined) {
      this._diagramService.InitEvent.subscribe((event) => {
        switch (event) {
          case "Config":
            this._scales = this._diagramService.getScale();
            this._metrics = this._diagramService.getMetric();
            this._scales.forEach(scale => {
              let metric = this._metrics.find((m => m.Title === scale.Metric));
              scale.Units = cloneDeep(metric.Units)
              scale.Interval = scale.Units.length - 1;
              scale.UnitIndex = scale.Units.findIndex(unit => unit.Label === scale.ActualScale)
              scale.PrevIndex = scale.Units.findIndex(unit => unit.Label === scale.DefaultScale)
            })
            this._initEvent.emit();
            break;
        }
      })
    } else {
      this._scales.forEach(scale => {
        let metric = this._metrics.find((m => m.Title === scale.Metric));
        scale.Units = cloneDeep(metric.Units)
        scale.Interval = scale.Units.length - 1;
        scale.UnitIndex = scale.Units.findIndex(unit => unit.Label === scale.ActualScale)
        scale.PrevIndex = scale.Units.findIndex(unit => unit.Label === scale.DefaultScale)
      })
    }
  }
  get Scales() {
    return this._scales;
  }

  get InitEvent() {
    return this._initEvent;
  }

  get ScaleChangeEvent() {
    return this._scaleChangeEvent;
  }

  get ResetEvent(){
    return this._resetEvent;
  }

  public changeScale(scale: Scale) {
    this._scaleChangeEvent.emit(scale);
  }

  public reset() {
    this._resetEvent.emit()
  }
}


