export class Scale{
    constructor(private _metric: string, private _actualscale: number,private _defaultScale: number,private _name: string){}

    get Metric(){
        return this._metric;
    }

    get DefaultScale(){
        return this._defaultScale;
    }

    get ActualScale(){
        return this._actualscale
    }

    set ActualScale(scale :number){
        this._actualscale = scale;
    }

    get Name(){
        return this._name;
    }

    set Name(name :string){
        this._name = name;
    }
}