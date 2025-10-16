import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BranchManageRoutingModule } from './branch-manage-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {BranchesComponent} from './branches.component';
import {BranchesAddEditComponent} from './branches-add-edit.component';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';

@NgModule({
  imports: [
    BranchManageRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ BranchesComponent, BranchesAddEditComponent ]
})
export class BranchManageModule { }
