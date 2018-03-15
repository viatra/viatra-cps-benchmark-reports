import { Build } from "./build";

export class ResultConfig {

    set Build(build: Array<Build>){
        this._build = build;
    }

    get Build(){
        return this._build;
    } 

    constructor(private _build: Array<Build>){}
}