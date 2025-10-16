import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {CompanyManageRoutingModule} from './company-manage-routing.module';
import {CompaniesComponent} from './companies.component';
import {CompaniesAddEditComponent} from './companies-add-edit.component';

@NgModule({
  imports: [
    CompanyManageRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ CompaniesComponent, CompaniesAddEditComponent ]
})
export class CompanyManageModule { }
