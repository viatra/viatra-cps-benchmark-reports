import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/Rx';

@Injectable()
export class JsonService {
 
  constructor(private http: Http) {}
  getResults() {
    return this.http.get("results/results.json").map((res:Response) => res.json());
  }
}
