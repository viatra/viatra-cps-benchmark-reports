import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { JsonService } from '../../services/json.service';
import { ResultsData } from '../../model/resultData';
import { DragulaService } from 'ng2-dragula';
import { Benchmark } from '../../model/benchmark';
import { DiagramService } from '../../diagram/service/diagram.service';
import { Scenario, Diagram } from '../../model/scenario';
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
  builds: Array<{ caseName: string, builds: Array<{ caseName: string, buildName: string }> }>;
  selectedBuilds: Array<{ caseName: string, buildName: string }>;
  selectedCases: Array<string>;
  step: number;
  cases: Array<String>;
  scenarioTitle: String;
  operations: Array<{ buildName: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string }> }>;
  shows: Array<{ operationid: string, caseName: string, buildName: string }>;
  hides: Array<{ operationid: string, caseName: string, buildName: string }>;
  private _diagrams: Array<Diagram>;
  constructor(private _buildConfigService: BuildConfigService, private _dragulaService: DragulaService, private _diagramService: DiagramService, private _router: Router) {
    this.back = new EventEmitter<null>();
    this.builds = new Array<{ caseName: string, builds: Array<{ caseName: string, buildName: string }> }>();
    this.cases = new Array<string>();
    this.selectedCases = new Array<string>();
    this.selectedBuilds = new Array<{ caseName: string, buildName: string }>();
    this.operations = new Array<{ buildName: string, operations: Array<{ operationid: string, title: string, caseName: string, buildName: string }> }>();
    this.shows = new Array<{ operationid: string, title: string, caseName: string, buildName: string }>();
    this.hides = new Array<{ operationid: string, title: string, caseName: string, buildName: string }>();

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
    this.disabled = false;
    this.next["disabled"] = this.disabled;
    this.step--;
  }


  public select() {
    if (!this.disabled) {
      switch (this.step) {
        case 0:
          this.selectedCases.forEach(selectedCase => {
            this._buildConfigService.getBuildsbyCase(selectedCase, builds => {
              this.builds.push({ caseName: selectedCase, builds: builds })
            })
          })
          break;
        case 1:
          this.shows = new Array<{ operationid: string, caseName: string, title: string, buildName: string }>();
          this.hides = new Array<{ operationid: string, caseName: string, title: string, buildName: string }>();
          this.selectedBuilds.forEach(select => {
            this._buildConfigService.getBuildConfig(select.caseName, select.buildName, (buildConfig => {
              this.operations.push(buildConfig);
            })
            )
          })
          break;
        case 2:
          console.log(this.shows)
          this._diagrams = new Array<Diagram>();
          this.shows.forEach(element => {
            this._diagrams.push({ caseName: element.caseName, buildName: element.buildName, operationid: element.operationid, opened: true });
          })
          this.hides.forEach(element => {
            this._diagrams.push({ caseName: element.caseName, buildName: element.buildName, operationid: element.operationid, opened: false });
          })
          break;

        case 3:
          this._diagramService.addScenario(new Scenario(this._diagrams, this.scenarioTitle));
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
        this.disabled = this.selectedCases.length === 0;
        break;
      case 1:
        this.disabled = this.selectedBuilds.length === 0;
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
    this._buildConfigService.getCaseName((cases => {
      this.cases = cases;
    }))
  }
}