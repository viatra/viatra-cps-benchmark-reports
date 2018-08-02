import { Component, OnInit, Injectable } from '@angular/core';
import { Scale } from '../model/defaultScale';
import { DiagramService } from '../diagram/service/diagram.service';
import { SliderService } from './slider.service';
import { interval } from '../../../node_modules/rxjs/observable/interval';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.css']
})


export class SliderComponent implements OnInit {
  selectedSlider: Scale;
  scales: Array<Scale>;
  constructor(private _sliderService: SliderService) {
    this.scales = new Array<Scale>();
  }

  public getLabel(): string{
    return this.selectedSlider.ActualScale
  }

  change(){
    this.selectedSlider.ActualScale = this.selectedSlider.Units[this.selectedSlider.UnitIndex].Label
    console.log(this.selectedSlider)
    this._sliderService.changeScale(this.selectedSlider)
    this.selectedSlider.PrevIndex = this.selectedSlider.UnitIndex
  }

  /*public changeSlider(slider: string){
    this.selectedSlider =  this.scales.find(scale =>{
      return scale.Metric === slider;
     });
     this.change(this.selectedSlider);
      this._sliderService.changeScale(this.selectedSlider);
    }*/

  ngOnInit() {
    this.scales = this._sliderService.Scales;
    if (this.scales === null || this.scales === undefined) {
      this._sliderService.InitEvent.subscribe(() => {
        this.scales = this._sliderService.Scales;
        this.selectedSlider =  this.scales[0]
        this._sliderService.changeScale(this.selectedSlider)
      })
    }else{
      this.scales = this._sliderService.Scales;
      this.selectedSlider =  this.scales[0]
      
      this._sliderService.changeScale(this.selectedSlider)
    }
    console.log(this.selectedSlider)
  }
}
