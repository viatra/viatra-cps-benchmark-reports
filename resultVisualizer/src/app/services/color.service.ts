import { Injectable } from '@angular/core';
import { Color } from '../model/color';
import { Http, Response } from '@angular/http';
import { JsonService } from './json.service';
import { Config } from '../model/config';
import 'rxjs/Rx';
@Injectable()
export class ColorService {


  constructor(private _jsonService: JsonService) {}

  public getColors(configPath: string){
    return this._jsonService.getDiagramConfig(configPath).map((config: Config)=> config.ToolColor);
  }

}
