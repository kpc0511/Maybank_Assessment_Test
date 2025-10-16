import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {RanksRoutingModule} from './rank-routing.module';
import {RankTypesComponent} from './rank-types.component';
import {RankTypeAddEditComponent} from './rank-type-add-edit.component';
import {RankDetailsComponent} from './rank-details.component';
import {RankDetailsAddEditComponent} from './rank-details-add-edit.component';

@NgModule({
  imports: [
    RanksRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule
  ],
  declarations: [ RankTypesComponent, RankTypeAddEditComponent, RankDetailsComponent, RankDetailsAddEditComponent ]
})
export class RankModule { }
