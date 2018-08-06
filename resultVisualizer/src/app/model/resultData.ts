export class ResultsData{

    set DiagramType(diagramType: string){
        this._diagramType = diagramType;
    }

    get DiagramType(){
        return this._diagramType;
    }

    set OperationID(operationID: String){
        this._operationID = operationID;
    }

    get OperationID(){
        return this._operationID;
    }

    set Title(title: string){
        this._title = title;
    }

    get Title(){
        return this._title;
    }

    set XLabel(xLabel: string){
        this._xLabel = xLabel;
    }

    get XLabel(){
        return this._xLabel;
    }
    set YLabel(yLabel: string){
        this._yLabel = yLabel;
    }

    get YLabel(){
        return this._yLabel;
    }

    get YType(){
        return this._yType;
    }

    get XType(){
        return this._xType;
    }

    get Metric(){
        return this._metric;
    }

    constructor(private _diagramType: string,
                private _operationID: String,
                private _title: string,
                private _xLabel: string,
                private _yLabel: string,
                private _metric: string,
                private _xType: string,
                private _yType: string
                ){}
}