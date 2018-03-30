import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ChartModule } from 'angular2-chartjs';
import { Routes, RouterModule } from "@angular/router"
import { FormsModule } from "@angular/forms";
import { AppComponent } from './app.component';
import { JsonService } from './services/json.service';
import { Http, HttpModule } from '@angular/http';
import { ColorService } from './services/color.service';
import { DiagramComponent } from './diagram/component/diagram.component';
import { DiagramService } from './diagram/service/diagram.service';
import { DropdownDiretive } from './directive/dropdown.directive';
import { ConfigService } from './services/config.service';
import { DiagramTitleComponent } from './title/diagram-title/diagram-title.component';
import { LegendTitleComponent } from './title/legend-title/legend-title.component';

import { MatSliderModule} from '@angular/material/slider';
import { ContainerComponent } from './diagram/container/container.component';
import { DashboardComponent } from './dashboard/dashboard/dashboard.component';
import { Scenario } from './model/scenario';
import { SliderService } from './slider/slider.service';
import { SliderComponent } from './slider/slider.component';
import { LoadingComponent } from './dashboard/loading/loading.component';
import { CreatingComponent } from './dashboard/creating/creating.component';
import { DragulaModule }  from 'ng2-dragula';
import { DropDownDirective } from './directive/drop-down.directive'
import { ComponentService } from './component.service';

const appRoutes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'diagrams', component: ContainerComponent},
  { path: 'loadScenario', component: LoadingComponent},
  {path: 'createScenario', component: CreatingComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    DiagramComponent,
    DropdownDiretive,
    DiagramTitleComponent,
    LegendTitleComponent,
    ContainerComponent,
    DashboardComponent,
    SliderComponent,
    LoadingComponent,
    CreatingComponent,
    DropDownDirective
  ],
  imports: [
    BrowserModule,
    ChartModule,
    HttpModule, 
    FormsModule, 
    MatSliderModule,
    RouterModule.forRoot(appRoutes),
    DragulaModule
  ], 
  providers: [
    JsonService,
    ColorService, 
    DiagramService, 
    ConfigService, 
    SliderService,
    ComponentService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
