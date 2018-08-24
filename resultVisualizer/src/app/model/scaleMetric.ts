

export class ScaleMetric {
    constructor(public Units: Array<Unit>, public Title: string) { }
}

export class Unit {
    constructor(public Label: string, public Value: Array<number>) { }
}