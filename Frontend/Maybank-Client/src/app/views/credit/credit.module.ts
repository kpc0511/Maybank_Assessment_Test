import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {CreditRoutingModule} from './credit-routing.module';
import {CreditsComponent} from './credits.component';
import {CreditsAddEditComponent} from './credits-add-edit.component';

@NgModule({
  imports: [
    CreditRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ CreditsComponent, CreditsAddEditComponent ]
})
export class CreditModule { }
