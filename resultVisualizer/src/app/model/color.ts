export class Color{
    set Color(color : string){
        this._color = color;
    }

    get Color(){
        return this._color;
    }

    set ToolName(toolName: String){
        this._tootlName = toolName;
    }

    get ToolName(){
        return this._tootlName;
    }
    
    constructor(private _tootlName: String, private _color: string){}
}