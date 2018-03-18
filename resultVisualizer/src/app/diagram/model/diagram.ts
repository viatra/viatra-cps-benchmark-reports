
import { Option } from "./option";
import { Data } from "./data";
import { DiagramService } from "../service/diagram.service";

export class Diagram {
    public scale: number;

    constructor(public type: string, public data: Data, public options: Option, public title: string, public metric){ }

}


