export class Option {
    legend: Legend;
    maintainAspectRatio: boolean;
    responsive: boolean;
    scales: Scales;
}


interface Scales {
    yAxes: Array<yAxes>;
    xAxes: Array<Axes>;
}


interface Legend{
    position?: string;
    display?: boolean;
}

interface yAxes extends Axes{
    ticks: Ticks;
    type: string;
}

interface Ticks{
    callback: (tick, index, ticks) => number;
    min: number;
}

interface Axes{
    scaleLabel: ScaleLabel;
}


interface ScaleLabel{
    display: boolean;
    labelString: string;
}