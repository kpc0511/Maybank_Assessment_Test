import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import {SongRoutingModule} from './song-routing.module';
import {SongsComponent} from './songs.component';
import {SongsAddEditComponent} from './songs-add-edit.component';

@NgModule({
  imports: [
    SongRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule,
    NgMultiSelectDropDownModule
  ],
  declarations: [ SongsComponent, SongsAddEditComponent]
})
export class SongModule { }
