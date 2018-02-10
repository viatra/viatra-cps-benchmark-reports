import { Injectable } from '@angular/core';

@Injectable()
export class ColorService {
  private _colors: string[] = [
    "rgba(255, 0, 0, 0.5)",
    "rgba(255, 255, 0, 0.5)",
    "rgba(0, 0, 0, 0.5)",
    "rgba(0, 255, 0, 0.5)",
    "rgba(255, 255, 0, 0.5)",
    "rgba(255, 0, 255, 0.5)",
    "rgba(0, 0, 255, 0.5)",
    "rgba(0, 255, 255, 0.5)",
    "rgba(20, 206, 255, 0.5)",
    "rgba(75, 192, 192, 0.5)",
    "rgba(111, 102, 255, 0.5)",
    "rgba(80, 159, 64, 0.5)"
  ]

  constructor() { }

  get colors(){
    return this._colors;
  }
}
