export class ResultsData{

    set DiagramType(diagramType: String){
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

    set Title(title: String){
        this._title = title;
    }

    get Title(){
        return this._title;
    }

    set XLabel(xLabel: String){
        this._xLabel = xLabel;
    }

    get XLabel(){
        return this._xLabel;
    }
    set YLabel(yLabel: String){
        this._yLabel = yLabel;
    }

    get YLabel(){
        return this._yLabel;
    }

    constructor(private _diagramType: String,
                private _operationID: String,
                private _title: String,
                private _xLabel: String,
                private _yLabel: String
                ){}
}