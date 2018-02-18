import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ChartModule } from 'angular2-chartjs';

import { AppComponent } from './app.component';
import { JsonService } from './services/json.service';
import { Http, HttpModule } from '@angular/http';
import { ColorService } from './services/color.service';
import { DiagramComponent } from './diagram/component/diagram.component';
import { DiagramService } from './diagram/service/diagram.service';


@NgModule({
  declarations: [
    AppComponent,
    DiagramComponent
  ],
  imports: [
    BrowserModule, ChartModule, HttpModule  
  ],
  providers: [JsonService , ColorService,DiagramService],
  bootstrap: [AppComponent]
})
export class AppModule { }
