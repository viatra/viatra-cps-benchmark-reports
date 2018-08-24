import { Injectable, EventEmitter } from '@angular/core';
import { DiagramService } from '../diagram/service/diagram.service';
import { Scale } from '../model/defaultScale';
import { Observable } from 'rxjs/Observable';


@Injectable()
export class SliderService {

  private _scales: Array<Scale>
  private _scaleChangeEvent: EventEmitter<Scale>
  private _initEvent: EventEmitter<null>
  private _resetEvent: EventEmitter<null>
  constructor(private _diagramService: DiagramService) {
    this._initEvent = new EventEmitter<null>();
    this._resetEvent = new EventEmitter<null>();
    this._scaleChangeEvent = new EventEmitter<Scale>();
    this._scales = this._diagramService.getScale();
    if (this._scales === null || this._scales === undefined) {
      this._diagramService.InitEvent.subscribe((event) => {
        switch (event) {
          case "Config":
            this._scales = this._diagramService.getScale();
            this._scales.forEach(scale => {
              scale.Interval = scale.Units.length - 1;
              scale.ActualScale = scale.DefaultScale;
              scale.UnitIndex = scale.Units.findIndex(unit => unit.Label === scale.ActualScale)
              scale.PrevIndex = scale.UnitIndex
            })
            this._initEvent.emit();
            break;
        }
      })
    } else {
      this._scales.forEach(scale => {
        scale.Interval = scale.Units.length - 1;
        scale.ActualScale = scale.DefaultScale;
        scale.UnitIndex = scale.Units.findIndex(unit => unit.Label === scale.ActualScale)
        scale.PrevIndex = scale.UnitIndex
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


