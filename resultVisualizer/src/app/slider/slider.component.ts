import { Component, OnInit, Injectable } from '@angular/core';
import { Scale } from '../model/defaultScale';
import { DiagramService, TimeScale, MemoryScale } from '../diagram/service/diagram.service';
import { SliderService } from './slider.service';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.css']
})


export class SliderComponent implements OnInit {
  selectedSlider: Scale;
  scales: Array<Scale>;
  constructor(private _sliderService: SliderService) {
    this.selectedSlider = new Scale("Time",-9,-9,"ns");
    this.scales = new Array<Scale>();
}


change(slider : Scale){
  switch(slider.Metric){
    case "Time":
      slider.Name = TimeScale[slider.ActualScale];
    break;
    case "Memory":
      slider.Name = MemoryScale[slider.ActualScale];
    break;
  }
  this._sliderService.changeScale(this.selectedSlider);
}
public changeSlider(slider: string){
  this.selectedSlider =  this.scales.find(scale =>{
    return scale.Metric === slider;
   });
   this.change(this.selectedSlider);
    this._sliderService.changeScale(this.selectedSlider);
  }

  ngOnInit() {
    this.scales = this._sliderService.Scales;
    if( this.scales === null ||  this.scales === undefined){
       this._sliderService.InitEvent.subscribe(()=>{
       this.scales = this._sliderService.Scales;
        this.selectedSlider = this.scales[0];
        this.change(this.selectedSlider);
      })
    }
  }
}
