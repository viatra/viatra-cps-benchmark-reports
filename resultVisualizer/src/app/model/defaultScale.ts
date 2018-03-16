export class DefaultScale{
    constructor(private _metric: string, private _scale: number,private _name: string){}

    get Metric(){
        return this._metric;
    }

    get Scale(){
        return this._scale;
    }

    get Name(){
        return this._name;
    }

    set Name(name :string){
        this._name = name;
    }
}