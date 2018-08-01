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
  selectedCases: Array<{buildName :string, caseName: string}>;
  step: number;
  cases: Array<{buildName :string, cases:Array<{buildName :string, caseName: string}>}>;
  scenarios: Array<{caseName :string, scenarios: Array<{caseName :string, title: string}>}>;
  selectedScenarios: Array<{caseName :string, title: string}>;
  scenarioTitle: String;
  operations: Array<{ buildName: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string }> }>;
  shows: Array<{ operationid: string, scenario: string, caseName: string, buildName: string }>;
  hides: Array<{ operationid: string, scenario: string, caseName: string, buildName: string }>;
  private _diagrams: Array<Diagram>;
  constructor(private _buildConfigService: BuildConfigService, private _dragulaService: DragulaService, private _diagramService: DiagramService, private _router: Router) {
    this.back = new EventEmitter<null>();
    this.builds = new Array<string>();
    this.cases =  new Array<{buildName :string, cases:Array<{buildName :string, caseName: string}>}>();
    this.selectedCases = new Array<{buildName :string, caseName: string}>();
    this.selectedScenarios = new Array<{caseName :string, title: string}>();
    this.selectedBuilds = new Array<string>();
    this.scenarios = new Array<{caseName :string, scenarios: Array<{caseName :string, title: string}>}>();
    this.operations = new Array<{ buildName: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string }> }>();
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

  public addAllCasesFromBuild(buildId: string){
    let tmp =  this.cases.find(function(elemet){
      return elemet.buildName === buildId
    }).cases
    this.selectedCases = this.selectedCases.concat(tmp)
    this.cases.find(function(elemet){
      return elemet.buildName === buildId
    }).cases = []

    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public addAllScenarioFromCases(caseName :string){
    let tmp =  this.scenarios.find(function(elemet){
      return elemet.caseName === caseName
    }).scenarios

    this.selectedScenarios = this.selectedScenarios.concat(tmp)
    this.scenarios.find(function(elemet){
      return elemet.caseName === caseName
    }).scenarios = []

    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public removeAllBuilds() {
    this.builds = this.builds.concat(this.selectedBuilds);
    this.selectedBuilds = [];
    this.disabled = false;
    this.next["disabled"] = this.disabled;
  }

  public removeAllCases(){
    let cases = this.cases;
    this.selectedCases.forEach(function(c){
      cases.find(function(elemet){
        return elemet.buildName === c.buildName
      }).cases.push(c)
    })
    this.cases = cases;
    this.selectedCases = []
  }

  public removeAllScenario(){
    let scenarios = this.scenarios;
    this.selectedScenarios.forEach(function(scenario){
      scenarios.find(function(elemet){
        return elemet.caseName === scenario.caseName
      }).scenarios.push(scenario)
    })
    this.scenarios = scenarios;
    this.selectedScenarios = []
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
          this.selectedCases.forEach(selectedCase =>{
            this._buildConfigService.getScenariosByCase(selectedCase.buildName, selectedCase.caseName, scenarios=>{
              this.scenarios.push({caseName: `${selectedCase.buildName}/${selectedCase.caseName}`, scenarios: scenarios})
            })
          })
         break;
       /* case 1:
          this.shows = new Array<{ operationid: string, caseName: string, scenario: string, title: string, buildName: string }>();
          this.hides = new Array<{ operationid: string, caseName: string, scenario: string, title: string, buildName: string }>();
          this.selectedBuilds.forEach(select => {
            this._buildConfigService.getBuildConfig(select.caseName,select.scenario, select.buildName, (buildConfig => {
              this.operations.push(buildConfig);
            })
            )
          })
          break;
        case 2:
          this._diagrams = new Array<Diagram>();
          this.shows.forEach(element => {
            this._diagrams.push({ caseName: element.caseName, build: element.buildName, scenario: element.scenario, operationid: element.operationid, opened: true });
          })
          this.hides.forEach(element => {
            this._diagrams.push({ caseName: element.caseName, build: element.buildName, scenario: element.scenario, operationid: element.operationid, opened: false });
          })
          break;
 
        case 3:
          this._diagramService.addScenario(new DiagramSet(this._diagrams, this.scenarioTitle));
          this._router.navigate(['diagrams'], { queryParams: { 'scenario': 1, "type": "created" } });
          break;*/
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
        this.disabled = this.shows.length === 0 && this.hides.length === 0;
        break;
      case 3:
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