import { Tool } from "./tool";

export class Benchmark {
    Path: string;
    public Results: Results[];
}

export class Results{
    constructor(public operationID: string, public tool: Tool[]){}
}
