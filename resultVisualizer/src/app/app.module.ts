import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { ChartModule } from 'angular2-chartjs';

import {FormsModule } from "@angular/forms";
import { AppComponent } from './app.component';
import { JsonService } from './services/json.service';
import { Http, HttpModule } from '@angular/http';
import { ColorService } from './services/color.service';
import { DiagramComponent } from './diagram/component/diagram.component';
import { DiagramService } from './diagram/service/diagram.service';
import { DropdownDiretive } from './directive/dropdown.directive';
import { ConfigService } from './services/config.service';


@NgModule({
  declarations: [
    AppComponent,
    DiagramComponent,
    DropdownDiretive
  ],
  imports: [
    BrowserModule, ChartModule, HttpModule, FormsModule 
  ], 
  providers: [JsonService , ColorService, DiagramService, ConfigService],
  bootstrap: [AppComponent]
})
export class AppModule { }
