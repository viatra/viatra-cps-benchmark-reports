export class Legend{
    constructor(private _title: string, private _color: string, private _count: number){}

    set Title(title : string){
        this._title = title;
    }

    get Title(){
        return this._title;
    }

    set Color(color: string){
        this._color = color;
    }

    get Color(){
        return this._color;
    }

    set Count(count: number){
        this._count = count;
    }

    get Count(){
        return this._count;
    }
}