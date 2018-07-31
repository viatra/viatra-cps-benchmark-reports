export class DiagramSet {
    constructor(public Diagrams : Array<Diagram>, public Title : String){}
}

export class Diagram {
    constructor(public CaseName: string, public Build: string,public Scenario: string, public OperationId: string,public Opened: boolean){}
}

