import { Injectable } from '@angular/core';
import { JsonService } from './json.service';
import 'rxjs/Rx';
import { Config } from '../model/config';
@Injectable()
export class ConfigService {

  constructor(private _jsonService: JsonService) { }

  public getResultConfig(configPath: string) {
    return this._jsonService.getDiagramConfig(configPath).map((config: Config) => {
      return config
    });
  }
}
