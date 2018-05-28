import { Color } from "./color";
import { Scale } from "./defaultScale";

export class Config {

    set ToolColor(toolColor: Array<Color>) {
        this._toolColor = toolColor;
    }

    get ToolColor() {
        return this._toolColor;
    }

    get Scale() {
        return this._scale;
    }

    constructor(private _toolColor: Array<Color>, private _scale: Array<Scale>) { }
}