import { Build } from "./build";
import { Scale } from "./defaultScale";

export class ResultConfig {

    set Build(build: Array<Build>){
        this._build = build;
    }

    get Build(){
        return this._build;
    } 


    get Scale(){
        return this._scale;
    }

    constructor(private _build: Array<Build>, private _scale : Array<Scale>){}
}