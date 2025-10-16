import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {TablesComponent} from '../branch-table/tables.component';
import {TableManageRoutingModule} from './table-manage-routing.module';
import {TableAddEditComponent} from './table-add-edit.component';

@NgModule({
  imports: [
    TableManageRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule
  ],
  declarations: [ TablesComponent, TableAddEditComponent ]
})
export class TableManageModule { }
