export class Scenario {
    constructor(public diagrams : Array<Diagram>, public name : String){}
}


class Diagram {
    constructor(public build: String, public result : Result){}
}

class Result{
    constructor(public opened: Array<String>, public closed: Array<String>){}
}
