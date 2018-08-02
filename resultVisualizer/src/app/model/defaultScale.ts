export class Scale {
    constructor(public Metric: string,
        public ActualScale: string,
        public DefaultScale: string,
        public Interval: number,
        public UnitIndex: number,
        public Units: Array<Unit>) { }
}

export class Unit{
    constructor (public Label:string, public Value: Array<number>){}
}