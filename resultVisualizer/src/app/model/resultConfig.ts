import { Build } from "./build";
import { DefaultScale } from "./defaultScale";

export class ResultConfig {

    set Build(build: Array<Build>){
        this._build = build;
    }

    get Build(){
        return this._build;
    } 


    get DefaultScale(){
        return this._defaultScale;
    }

    constructor(private _build: Array<Build>, private _defaultScale : Array<DefaultScale>){}
}