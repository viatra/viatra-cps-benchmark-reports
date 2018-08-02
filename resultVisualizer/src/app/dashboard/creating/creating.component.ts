import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { ResultsData } from '../../model/resultData';
import { DragulaService } from 'ng2-dragula';
import { Benchmark } from '../../model/benchmark';
import { DiagramService } from '../../diagram/service/diagram.service';
import { DiagramSet, Diagram } from '../../model/diagramSet';
import { Router } from '@angular/router'
import { BuildConfigService } from '../../services/build.config.service';

@Component({
  selector: 'app-creating',
  templateUrl: './creating.component.html',
  styleUrls: ['../dashboard/dashboard.component.css']
})
export class CreatingComponent implements OnInit {
  @Output() back: EventEmitter<null>
  next = {
    "glyphicon": true,
    "glyphicon-arrow-right": true,
    "disabled": true
  }
  disabled: boolean;
  builds: Array<string>;
  selectedBuilds: Array<string>;
  selectedCases: Array<{ buildName: string, caseName: string }>;
  step: number;
  cases: Array<{ buildName: string, cases: Array<{ buildName: string, caseName: string }> }>;
  scenarios: Array<{ id: string, scenarios: Array<{ id: string, buildName: string, caseName: string, scenario: string }> }>;
  selectedScenarios: Array<{ id: string, buildName: string, caseName: string, scenario: string }>;
  scenarioTitle: String;
  operations: Array<{ id: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string, scenario: string }> }>;
  shows: Array<{ operationid: string, scenario: string, caseName: string, buildName: string }>;
  hides: Array<{ operationid: string, scenario: string, caseName: string, buildName: string }>;
  private _diagrams: Array<Diagram>;
  constructor(private _buildConfigService: BuildConfigService, private _dragulaService: DragulaService, private _diagramService: DiagramService, private _router: Router) {
    this.back = new EventEmitter<null>();
    this.builds = new Array<string>();
    this.cases = new Array<{ buildName: string, cases: Array<{ buildName: string, caseName: string }> }>();
    this.selectedCases = new Array<{ buildName: string, caseName: string }>();
    this.selectedScenarios = new Array<{ id: string, buildName: string, caseName: string, scenario: string }>();
    this.selectedBuilds = new Array<string>();
    this.scenarios = new Array<{ id: string, scenarios: Array<{ id: string, buildName: string, caseName: string, scenario: string }> }>();
    this.operations = new Array<{ id: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string, scenario: string }> }>();
    this.shows = new Array<{ operationid: string, title: string, scenario: string, caseName: string, buildName: string }>();
    this.hides = new Array<{ operationid: string, title: string, scenario: string, caseName: string, buildName: string }>();

    this.disabled = true;
    this.step = 0;
    const bag: any = this._dragulaService.find('result');
    if (bag !== undefined) this._dragulaService.destroy('result');
    this._dragulaService.setOptions('result', {
      accepts: (el, target, source, sibling) => {
        return target.id === ""
      },
      revertOnSpill: true
    });

    this._dragulaService.dropModel.subscribe((value) => {
      this.onDropModel(value.slice(1));
    });
  }

  public clickedBack() {
    this.back.emit();
    if (this.step === 0) {
      this._router.navigate(['/'])
    } else {
      this.disabled = false;
      this.next["disabled"] = this.disabled;
      this.step--;
    }
  }

  public addAllBuilds() {
    this.selectedBuilds = this.selectedBuilds.concat(this.builds);
    this.builds = [];
    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public addAllCasesFromBuild(buildId: string) {
    let tmp = this.cases.find(function (elemet) {
      return elemet.buildName === buildId
    }).cases
    this.selectedCases = this.selectedCases.concat(tmp)
    this.cases.find(function (elemet) {
      return elemet.buildName === buildId
    }).cases = []

    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public addAllScenarioFromCases(id: string) {
    let tmp = this.scenarios.find(function (elemet) {
      return elemet.id === id
    }).scenarios

    this.selectedScenarios = this.selectedScenarios.concat(tmp)
    this.scenarios.find(function (elemet) {
      return elemet.id === id
    }).scenarios = []

    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public removeAllBuilds() {
    this.builds = this.builds.concat(this.selectedBuilds);
    this.selectedBuilds = [];
    this.disabled = true;
    this.next["disabled"] = this.disabled;
  }

  public removeAllCases() {
    let cases = this.cases;
    this.selectedCases.forEach(function (c) {
      cases.find(function (elemet) {
        return elemet.buildName === c.buildName
      }).cases.push(c)
    })
    this.cases = cases;
    this.selectedCases = []
    this.disabled = true;
    this.next["disabled"] = this.disabled;
  }

  public removeAllScenario() {
    let scenarios = this.scenarios;
    this.selectedScenarios.forEach(function (scenario) {
      scenarios.find(function (elemet) {
        return elemet.id === scenario.id
      }).scenarios.push(scenario)
    })
    this.scenarios = scenarios;
    this.selectedScenarios = []
    this.disabled = true;
    this.next["disabled"] = this.disabled;
  }

  public select() {
    if (!this.disabled) {
      switch (this.step) {
        case 0:
          this.selectedBuilds.forEach(selectedBuild => {
            this._buildConfigService.getCasesByBuild(selectedBuild, cases => {
              this.cases.push({ buildName: selectedBuild, cases: cases })
            })
          })
          break;
        case 1:
          this.selectedCases.forEach(selectedCase => {
            this._buildConfigService.getScenariosByCase(selectedCase.buildName, selectedCase.caseName, scenarios => {
              this.scenarios.push({ id: `${selectedCase.buildName}/${selectedCase.caseName}`, scenarios: scenarios })
            })
          })
          break;
        case 2:
          this.shows = new Array<{ operationid: string, caseName: string, scenario: string, title: string, buildName: string }>();
          this.hides = new Array<{ operationid: string, caseName: string, scenario: string, title: string, buildName: string }>();
          this.selectedScenarios.forEach(select => {
            this._buildConfigService.getBuildConfig(select.buildName, select.caseName, select.scenario, (buildConfig => {
              this.operations.push({ id: `${select.buildName}/${select.caseName}/${select.scenario}`, operations: buildConfig });
            })
            )
          })
          break;
        case 3:
          this._diagrams = new Array<Diagram>();
          this.shows.forEach(element => {
            this._diagrams.push({ CaseName: element.caseName, Build: element.buildName, Scenario: element.scenario, OperationId: element.operationid, Opened: true });
          })
          this.hides.forEach(element => {
            this._diagrams.push({ CaseName: element.caseName, Build: element.buildName, Scenario: element.scenario, OperationId: element.operationid, Opened: false });
          })
          break;
        case 4:
          this._diagramService.addScenario(new DiagramSet(this._diagrams, this.scenarioTitle));
          this._router.navigate(['diagrams'], { queryParams: { 'scenario': 1, "type": "created" } });
          break;
      }
      this.disabled = true;
      this.next["disabled"] = this.disabled;
      this.step++;
    }
  }

  private onDropModel(args) {
    let [el, target, source] = args;
    switch (this.step) {
      case 0:
        this.disabled = this.selectedBuilds.length === 0;
        break;
      case 1:
        this.disabled = this.selectedCases.length === 0;
        break;
      case 2:
        this.disabled = this.selectedScenarios.length === 0;
        break;
      case 3:
        this.disabled = this.shows.length === 0 && this.hides.length === 0;
        break;
      case 4:
        this.disabled = this.scenarioTitle === "";
        break;
    }
    this.next["disabled"] = this.disabled;

  }

  public updateScenarioTitle() {
    this.disabled = this.scenarioTitle === "";
    this.next["disabled"] = this.disabled;
  }

  ngOnInit() {
    this._buildConfigService.getBuildNames((builds => {
      this.builds = builds;
    }))
  }
}