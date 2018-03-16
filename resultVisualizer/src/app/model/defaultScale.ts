export class DefaultScale{
    constructor(private _metric: string, private _scale: number){}

    get Metric(){
        return this._metric;
    }

    get Scale(){
        return this._scale;
    }
}