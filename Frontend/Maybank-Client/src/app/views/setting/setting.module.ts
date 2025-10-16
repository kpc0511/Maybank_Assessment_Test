// Angular
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ProfileComponent } from './profile.component';

import { SettingRoutingModule } from './setting-routing.module';
import {FormsModule} from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    SettingRoutingModule,
    FormsModule
  ],
  declarations: [
    ProfileComponent
  ]
})
export class SettingModule { }
