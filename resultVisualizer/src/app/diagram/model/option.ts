export class Option {
    legend: Legend;
    maintainAspectRatio: boolean;
    responsive: boolean;
    scales: Scales;
}


interface Scales {
    yAxes: Array<Axes>;
    xAxes: Array<Axes>;
}


interface Legend {
    position?: string;
    display?: boolean;
}

interface yAxes extends Axes {
    ticks: Ticks;
    type: string;
}

interface Ticks {
    callback?: (tick, index, ticks) => number | Number;
    min?: number | Number;
    max?: Number;
    autoSkip?: boolean;
    padding?: number;
    source?: string;
    stepSize?: Number;
    maxTicksLimit?: number;
    display?: boolean;
}

interface Axes {
    scaleLabel?: ScaleLabel;
    ticks?: Ticks;
    type?: string;
    distribution?: string;
}


interface ScaleLabel {
    display: boolean;
    labelString: string;
}