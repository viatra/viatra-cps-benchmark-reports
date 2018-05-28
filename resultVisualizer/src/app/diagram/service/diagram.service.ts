import { Injectable, EventEmitter } from '@angular/core';
import { Option } from '../model/option';
import { JsonService } from '../../services/json.service';
import { Result } from '../../model/result';
import { Benchmark } from '../../model/benchmark';
import { Data } from '../model/data';
import { Tool } from '../../model/tool';
import { Dataset } from '../model/dataset';
import { ColorService } from '../../services/color.service';
import { digest } from '@angular/compiler/src/i18n/serializers/xmb';
import { Diagram } from '../model/diagram';
import { query } from '@angular/core/src/render3/instructions';
import { Console } from '@angular/core/src/console';
import { Scenario } from '../../model/scenario';
import { Observable } from 'rxjs/Observable';
import { take } from 'rxjs/operator/take';
import { Config } from '../../model/config';
import { Color } from '../../model/color';
import { ConfigService } from '../../services/config.service';
import { Build } from '../../model/build';
import { ResultsData } from '../../model/resultData';
import { Scale } from '../../model/defaultScale';
import { Subscriber } from 'rxjs/Subscriber';
import { DiagramLabel } from '../container/container.component';
import { scan } from 'rxjs/operators/scan';
import { Router } from '@angular/router';
import { BuildConfigService } from '../../services/build.config.service';

@Injectable()
export class DiagramService {
    private _selectedTitle: string;
    private _selectionUpdate: EventEmitter<SelectionUpdateEvent>;
    private _legendUpdate: EventEmitter<LegendUpdateEvent>;
    private _initEvent: EventEmitter<String>;
    private _diagrams: Array<Diagram>;
    private _scenarios: Array<Scenario>;
    private _title: Array<Title>;
    private _colors: Array<Color>;
    public configPath: string = `config/diagram.config.json`;
    private _defaultScale: Array<Scale>;
    private _newScenario: Scenario;
    constructor(private _jsonService: JsonService, private _colorService: ColorService, private _configservice: ConfigService, private _router: Router, private _buildConfigService: BuildConfigService) {
        this._title = new Array<Title>();
        this._selectionUpdate = new EventEmitter<SelectionUpdateEvent>();
        this._legendUpdate = new EventEmitter<LegendUpdateEvent>();
        this._initEvent = new EventEmitter<null>();
        this._scenarios = new Array<Scenario>();

        this._colorService.getColors(this.configPath).subscribe((colors: Color[]) => {
            this._colors = colors;
        });

        this._configservice.getResultConfig(this.configPath).subscribe((scales: Array<Scale>) => {
            this._defaultScale = scales
            this._defaultScale.forEach(scale => {
                scale.ActualScale = scale.DefaultScale;
            })
            this._initEvent.emit("Config");
        });
    }


    get Scenarios() {
        return this._scenarios;
    }

    public createNewScenarion(scenario: Scenario) {
        return new Observable((observer) => {
            this._newScenario = scenario;
            observer.next();
            observer.complete();
        });

    }

    public addScenario(scenario: Scenario) {
        this._scenarios.push(scenario);
    }
    public runScenario(index: number, type: string): Observable<Boolean> {
        console.log("run scenario")
        return new Observable((observer) => {
            this._title = new Array<Title>();
            this._diagrams = new Array<Diagram>();
                let scenario;
                switch (type) {
                    case "created":
                        scenario = this._scenarios[this._scenarios.length - 1];
                }
                console.log(scenario)
                if (scenario === null || scenario === undefined) {
                    this._router.navigate(["/"])
                } else {
                    this.addDiagrams(scenario, observer);
                }
            })
    }

    private addDiagrams(scenario: Scenario, observer: Subscriber<Boolean>) {
        this._selectionUpdate.emit(new SelectionUpdateEvent("Clear", null));
        scenario.diagrams.forEach((diag, index) => {
            this._jsonService.getResults(diag.caseName, diag.buildName).subscribe((benchmarks: Benchmark[]) => {
                this._buildConfigService.getFullBuildConfig(diag.caseName, diag.buildName, configs => {
                    this.createDiagram(benchmarks.find((benchmark => benchmark.operationID === diag.operationid)), diag.opened, configs.ResultData.find((config => config.OperationID === diag.operationid)), configs.ID)
                    if (index === scenario.diagrams.length - 1) {
                        observer.next(true);
                        observer.complete();
                    }
                })
            });
        });
    }
    public getScale() {
        return this._defaultScale;
    }

    public sortDiagrams(titles: Array<DiagramLabel>) {
        this._selectionUpdate.emit(new SelectionUpdateEvent("Clear", null));
        titles.forEach(title => {
            let index = this._diagrams.findIndex((item, index) => {
                return item.title === title.title && title.ngClass["glyphicon-eye-open"] === true
            })
            if (index >= 0) {
                this._selectionUpdate.emit(new SelectionUpdateEvent("Added", this._diagrams[index]));
            }
        })
    }

    get InitEvent() {
        return this._initEvent;
    }

    get SelectiponUpdateEvent() {
        return this._selectionUpdate;
    }

    get LegendUpdateEvent() {
        return this._legendUpdate;
    }

    updateLegend(hide: boolean, toolName: string) {
        let type = hide === true ? "hide" : "show"
        this._legendUpdate.emit(new LegendUpdateEvent(type, toolName));
        this._diagrams.forEach(diagram => {
            diagram.data.datasets.forEach(dataset => {
                if (dataset.label === toolName) {
                    dataset.hidden = hide
                }
            })
        })
    }


    updateSelection(added: boolean, title: string) {
        let diagram = this._diagrams.find((diagram: Diagram) => {
            return diagram.title == title;
        });
        let type = added == true ? "Added" : "Removed";
        this._selectionUpdate.emit(new SelectionUpdateEvent(type, diagram));
    }

    private createDiagram(result: Benchmark, opened: boolean, operation: any, id: string) {
        if (result) {
            var data = new Data();
            let tools: Tool[] = result.tool;
            if (tools.length > 0) {
                let maxSizeTool: Tool = this.getMaxSizeTool(result);
                data.labels = this.getSizes(maxSizeTool);
                let index = 0;
                data.datasets = new Array<Dataset>();
                tools.forEach((tool: Tool) => {
                    data.datasets.push(this.getDataSet(tool, index));
                    index++;
                });
                let newDiagram = new Diagram(operation.DiagramType, data, this.getOption(operation.YLabel, operation.XLabel), `${operation.Title} (${id})`, operation.Metric);
                this._diagrams.push(newDiagram)
                this._selectionUpdate.emit(new SelectionUpdateEvent("Added", newDiagram));
            }
        }
    }


    private getDataSet(tool: Tool, index: number) {
        let dataset: Dataset = new Dataset();
        dataset.lineTension = 0;
        dataset.data = new Array();
        dataset.fill = false;
        dataset.label = tool.name
        dataset.borderColor = this.getColor(tool.name);
        dataset.backgroundColor = this.getColor(tool.name);
        tool.results.forEach((result: Result) => {
            dataset.data.push((result.metric.MetricValue));
        });
        return dataset;
    }

    get Title(): Array<Title> {
        return this._title;
    }


    public getColor(toolName: String): string {
        let color = this._colors.find((color: Color) => {
            return color.ToolName === toolName;
        }).Color;

        if (color === null || color === undefined) {
            color = "rgba(0, 0, 0, 1)"
        }
        return color;
    }

    private addTitle(build: Build, benchmark: Benchmark, opened: boolean) {
        let tmp = this.resolveOperation(build.ResultData, benchmark.operationID);
        let ngClass = opened ? {
            "glyphicon": true,
            "glyphicon-eye-open": true,
            "glyphicon-eye-close": false,
            "missing": (benchmark.tool.length === 0) ? true : false
        } :
            {
                "glyphicon": true,
                "glyphicon-eye-open": false,
                "glyphicon-eye-close": true,
                "missing": (benchmark.tool.length === 0) ? true : false
            }
        this._title.push(new Title(`${tmp.Title} (${build.ID})`, ngClass, tmp.OperationID))
    }

    public resolveOperation(resultDatas: Array<ResultsData>, operationID: String) {
        return resultDatas.find((resultData: ResultsData) => {
            return resultData.OperationID === operationID;
        })
    }

    private getSizes(tool: Tool): string[] {
        let sizes: string[] = [];
        tool.results.forEach((result: Result) => {
            sizes.push(result.size.toString());
        });
        return sizes;
    }

    private getMaxSizeTool(benchmark: Benchmark): Tool {
        let max: Tool = benchmark.tool[0];
        for (let i = 1; i < benchmark.tool.length; i++) {
            if (max.results.length < benchmark.tool[i].results.length) {
                max = benchmark.tool[i];
            }
        }
        return max;
    }

    getOption(yLabel: string, xLabel: string): Option {
        return {
            maintainAspectRatio: true,
            legend: {
                display: false
            },
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function (tick, index, ticks) {
                            return (index % 3 == 0) || (index == ticks.length - 1) ? tick.toLocaleString() : null;
                        },
                        min: 0
                    },
                    scaleLabel: {
                        display: true,
                        labelString: yLabel,
                    },
                    type: "logarithmic"
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: xLabel
                    }
                }]
            }
        }
    }
}


export class Title {
    constructor(private _value: string, private _ngclass: Glyphicon, private _ID: String) { }

    set NgClass(ngClass: Glyphicon) {
        this._ngclass = ngClass;
    }

    get ID() {
        return this._ID;
    }

    get NgClass() {
        return this._ngclass;
    }


    get Value() {
        return this._value;
    }
}


export class SelectionUpdateEvent {
    constructor(private _eventType: string, private _diagram: Diagram) { }
    get EventType() {
        return this._eventType;
    }

    get Diagram() {
        return this._diagram;
    }
}

export class LegendUpdateEvent {
    constructor(private _evenType: string, private _toolName: string) { }

    get EventType() {
        return this._evenType;
    }

    get ToolName() {
        return this._toolName;
    }
}

export enum TimeScale {
    "s" = 0,
    "ms" = -3,
    "µs" = -6,
    "ns" = -9,
    "ps" = -12,
}

export enum MemoryScale {
    "B" = 1,
    "KB" = 2,
    "MB" = 3,
    "GB" = 4
}


export interface Glyphicon {
    "missing": boolean,
    "glyphicon": boolean,
    "glyphicon-eye-open": boolean,
    "glyphicon-eye-close": boolean
}