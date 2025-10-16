import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {TermRoutingModule} from './term-routing.module';
import {TermsComponent} from './terms.component';
import {TermsAddEditComponent} from './terms-add-edit.component';

@NgModule({
  imports: [
    TermRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ TermsComponent, TermsAddEditComponent ]
})
export class TermModule { }
