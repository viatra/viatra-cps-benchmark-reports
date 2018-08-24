import { Unit } from "./scaleMetric";

export class Scale {

    constructor(public Metric: string,
        public Title: string,
        public ActualScale: string,
        public DefaultScale: string,
        public Interval: number,
        public UnitIndex: number,
        public PrevIndex: number,
        public Units: Array<Unit>) { }
}

