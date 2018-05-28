export class Scenario {
    constructor(public diagrams : Array<Diagram>, public name : String){}
}


export class Diagram {
    constructor(public caseName: string, public buildName: string, public operationid: string,public opened: boolean){}
}

