import { Injectable } from '@angular/core';
import { JsonService } from './json.service';

@Injectable()
export class BuildConfigService {



    private _buildist: Array<BuildList>;
    constructor(private _jsonService: JsonService) { }

    public getCaseName(callback: any) {
        if (!this._buildist) {
            this._jsonService.getBuilds().subscribe(results => {
                this._buildist = results;
                callback(results.map(build => {
                    return build.CaseName
                })
                )
            })
        } else {
            callback(this._buildist.map(build => {
                return build.CaseName
            })
            )
        }
    }

    public getBuildsbyCase(caseName: string, callback: any) {
        if (!this._buildist) {
            this._jsonService.getBuilds().subscribe(results => {
                this._buildist = results;
                this._buildist.forEach(element => {
                    if (element.CaseName === caseName) {
                        let builds = new Array<{ caseName: string, buildName: string }>()
                        element.Builds.forEach(build => {
                            builds.push({ caseName: caseName, buildName: build })
                        });
                        callback(builds)
                        return
                    }
                })
            })
        } else {
            this._buildist.forEach(element => {
                if (element.CaseName === caseName) {
                    let builds = new Array<{ caseName: string, buildName: string }>()
                    element.Builds.forEach(build => {
                        builds.push({ caseName: caseName, buildName: build })
                    });
                    callback(builds)
                    return
                }
            })
        }
    }

    public getBuildConfig(caseName: string, buildName: string, callback: any) {
        this._jsonService.getBuildConfig(caseName, buildName).subscribe(buildconfig => {
            let operations = new Array<{ operationid: string, title: string, caseName: string, buildName: string }>()
            buildconfig.ResultData.forEach(element => {
                operations.push({ operationid: element.OperationID, title: element.Title, caseName: caseName, buildName: buildName })
            });
            callback({ buildName: buildName, operations });
        })
    }
    public getFullBuildConfig(caseName: string, buildName: string, callback: any) {
        this._jsonService.getBuildConfig(caseName, buildName).subscribe(buildconfig => {
            callback(buildconfig);
        })
    }
}


class BuildList {
    constructor(public CaseName: string, public Builds: Array<string>) { }
}
