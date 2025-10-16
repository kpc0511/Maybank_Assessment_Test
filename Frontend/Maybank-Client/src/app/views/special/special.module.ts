import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {SpecialRoutingModule} from './special-routing.module';
import {GiftsComponent} from './gifts.component';
import {PromotionsComponent} from './promotions.component';
import {GiftsAddEditComponent} from './gifts-add-edit.component';
import {PromotionsAddEditComponent} from './promotions-add-edit.component';

@NgModule({
  imports: [
    SpecialRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ GiftsComponent, GiftsAddEditComponent, PromotionsComponent, PromotionsAddEditComponent ]
})
export class SpecialModule { }
