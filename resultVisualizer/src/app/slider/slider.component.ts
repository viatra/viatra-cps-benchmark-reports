import { Component, OnInit, Injectable, Input, OnDestroy } from '@angular/core';
import { Scale } from '../model/defaultScale';
import { SliderService } from './slider.service';
import { Observable, Subject } from 'rxjs';

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

  public getLabel(): string {
    return this.selectedSlider.ActualScale
  }

  change() {
    this.selectedSlider.ActualScale = this.selectedSlider.Units[this.selectedSlider.UnitIndex].Label
    this._sliderService.changeScale(this.selectedSlider)
    this.selectedSlider.PrevIndex = this.selectedSlider.UnitIndex
  }

  public changeSlider(slider: string) {
    this.selectedSlider.PrevIndex = this.selectedSlider.UnitIndex
    this.selectedSlider = this.scales.find(scale => {
      return scale.Title === slider;
    });
    this._sliderService.changeScale(this.selectedSlider);
  }

  private reset() {
    this.scales.forEach(scale => {
      scale.ActualScale = scale.DefaultScale;
      scale.UnitIndex = scale.Units.findIndex(unit => unit.Label === scale.ActualScale)
      scale.PrevIndex = scale.UnitIndex
    })
    this.selectedSlider.ActualScale = this.selectedSlider.DefaultScale
    this.selectedSlider.UnitIndex = this.selectedSlider.Units.findIndex(unit => unit.Label === this.selectedSlider.ActualScale)
    this.selectedSlider.PrevIndex = this.selectedSlider.UnitIndex

  }

  ngOnInit() {
    this._sliderService.ResetEvent.subscribe(() => {
      this.reset()
    })
    this.scales = this._sliderService.Scales;
    if (this.scales === null || this.scales === undefined) {
      this._sliderService.InitEvent.subscribe(() => {
        this.scales = this._sliderService.Scales;
        this.selectedSlider = this.scales[0]
        this._sliderService.changeScale(this.selectedSlider)
      })
    } else {
      this.scales = this._sliderService.Scales;
      this.selectedSlider = this.scales[0]

      this._sliderService.changeScale(this.selectedSlider)
    }
  }
}
