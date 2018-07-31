import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/Rx';

@Injectable()
export class JsonService {
 
  constructor(private http: Http) {}
  getResults(caseName: String,build: string,scenario: string) {
    return this.http.get(`results/${build}/${caseName}/${scenario}/results.json`).map((res:Response) => res.json());
  }

  getScenarios(){
    return this.http.get(`results/dashboard.json`).map((res:Response) => res.json());
  }
  
  getDiagramConfig(configPath: string){
    return this.http.get(configPath).map((res:Response) => res.json());
  }

  getBuildConfig(caseName: String,build: string,scenario: string) {
    return this.http.get(`results/${build}/${caseName}/${scenario}/diagram.config.json`).map((res:Response) => res.json());
  }

  getBuilds(){
    return this.http.get('results/builds.json').map((res :Response) => res.json());
  }
}
