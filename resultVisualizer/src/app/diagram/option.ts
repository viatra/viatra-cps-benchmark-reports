export class Option {
    responsive: boolean;
    maintainAspectRatio: boolean;
    legend: {
        position: string;
    };
    scales: {
        yAxes: [{
            ticks: {
                callback: (tick, index, ticks) => number,
                min: number
            },
            scaleLabel: {
                display: boolean,
                labelString: string,
            },
            type: string
        }],
        xAxes: [{
            scaleLabel: {
              display: boolean,
              labelString: string,
              type: string
            }
          }]
    }
}
