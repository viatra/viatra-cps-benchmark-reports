import { Injectable } from '@angular/core';
import { JsonService } from './json.service';

@Injectable()
export class BuildConfigService {



    private _buildist: Array<BuildList>;
    constructor(private _jsonService: JsonService) { }

    public getBuildNames(callback: any) {
        if (!this._buildist) {
            this._jsonService.getBuilds().subscribe(results => {
                this._buildist = results;
                callback(results.map(build => {
                    return build.BuildId
                })
                )
            })
        } else {
            callback(this._buildist.map(build => {
                return build.BuildId
            })
            )
        }
    }

    public getCasesByBuild(buildName: string, callback: any) {
        if (!this._buildist) {
            this._jsonService.getBuilds().subscribe(results => {
                this._buildist = results;
                this._buildist.forEach(element => {
                    if (element.BuildId === buildName) {
                        let cases = new Array<{ buildName: string, caseName: string }>()
                        element.Cases.forEach(c => {
                            cases.push({ buildName: buildName, caseName: c.CaseName })
                        });
                        callback(cases)
                        return
                    }
                })
            })
        } else {
            this._buildist.forEach(element => {
                if (element.BuildId === buildName) {
                    let cases = new Array<{ buildName: string, caseName: string }>()
                    element.Cases.forEach(c => {
                        cases.push({ caseName: c.CaseName, buildName: buildName })
                    });
                    callback(cases)
                    return
                }
            })
        }
    }


    public getScenariosByCase(buildId: string, caseName: string, callback: any) {
        if (!this._buildist) {
            this._jsonService.getBuilds().subscribe(results => {
                this._buildist = results;
                let cases = this._buildist.find(build => build.BuildId === buildId).Cases
                cases.forEach(element => {
                    if (element.CaseName === caseName) {
                        let scenarios = new Array<{ id: string, buildName: string, caseName: string, scenario: string }>()
                        element.Scenarios.forEach(scenario => {
                            scenarios.push({ id: `${buildId}/${caseName}`, buildName: buildId, caseName: caseName, scenario: scenario })
                        });
                        callback(scenarios)
                        return
                    }
                })
            })
        } else {
            let cases = this._buildist.find(build => build.BuildId === buildId).Cases
            cases.forEach(element => {
                if (element.CaseName === caseName) {
                    let scenarios = new Array<{ id: string, buildName: string, caseName: string, scenario: string }>()
                    element.Scenarios.forEach(scenario => {
                        scenarios.push({ id: `${buildId}/${caseName}`, buildName: buildId, caseName: caseName, scenario: scenario })
                    });
                    callback(scenarios)
                    return
                }
            })
        }
    }

    public getBuildConfig(buildName: string, caseName: string, scenario: string, callback: any) {
        this._jsonService.getBuildConfig(buildName,caseName, scenario).subscribe(buildconfig => {
            let operations = new Array<{id: string, operationid: string, scenario: string, title: string, caseName: string, buildName: string }>()
            buildconfig.ResultData.forEach(element => {
                operations.push({id: `${buildName}/${caseName}/${scenario}`,  operationid: element.OperationID, title: element.Title, caseName: caseName, buildName: buildName, scenario: scenario })
            });
            callback(operations);
        })
    }
    public getFullBuildConfig(buildName: string, caseName: string, scenario: string, callback: any) {
        this._jsonService.getBuildConfig(buildName, caseName, scenario).subscribe(buildconfig => {
            callback(buildconfig);
        })
    }
}


class BuildList {
    constructor(public BuildId: string, public Cases: Array<Cases>) { }
}

class Cases {
    constructor(public CaseName: string, public Scenarios: Array<string>) { }
}
