import { Color } from "./color";
import { ResultConfig } from "./resultConfig";

export class Config {

    set ToolColor(toolColor : Array<Color>){
        this._toolColor = toolColor;
    }

    get ToolColor(){
        return this._toolColor;
    }

    set ResultConfig(resultConfig: ResultConfig){
        this._resultConfig = resultConfig;
    }

    get ResultConfig(){
        return this._resultConfig;
    }

    constructor(private _toolColor: Array<Color>,private _resultConfig: ResultConfig){}
}