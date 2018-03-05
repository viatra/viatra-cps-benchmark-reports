import { ResultsData } from "./resultData";

export class Build{

    set ID(id: String){
        this._id = id;
    }

    get ID(){
        return this._id;
    }

    set ResultData(resultData: Array<ResultsData>){
        this._resultData = resultData;
    }

    get ResultData(){
        return this._resultData;
    }

    constructor(private _id: String,private _resultData: Array<ResultsData>){}  
}