import { Injectable, EventEmitter } from '@angular/core';
import { DiagramService } from '../diagram/service/diagram.service';
import { Scale } from '../model/defaultScale';


@Injectable()
export class SliderService {
  private _initEvent : EventEmitter<Scale[]>
  private _scaleChangeEvent : EventEmitter<Scale>
  constructor(private _diagramService: DiagramService) {
    this._initEvent = new EventEmitter<Scale[]>();
    this._scaleChangeEvent = new EventEmitter<Scale>();
    this._diagramService.InitEvent.subscribe((event) =>{
      switch(event){
        case "Config":
          this._initEvent.emit(this._diagramService.getScale());
          break;
      }
   });
  }
   get InitEvent(){
     return this._initEvent;
   }

   get ScaleChangeEvent(){
     return this._scaleChangeEvent;
   }

   public changeScale(scale :Scale){
     this._scaleChangeEvent.emit(scale);
   }
  }

  
