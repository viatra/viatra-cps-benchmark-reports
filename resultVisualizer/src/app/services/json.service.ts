import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/Rx';

@Injectable()
export class JsonService {
 
  constructor(private http: Http) {}
  getResults(build: string) {
    return this.http.get(`results/${build}/results.json`).map((res:Response) => res.json());
  }

  getScenarios(){
    return this.http.get(`config/example.scenario.json`).map((res:Response) => res.json());
  }

  getDiagramConfig(){
    return this.http.get(`config/diagram.config.json`).map((res:Response) => res.json());
  }

}
