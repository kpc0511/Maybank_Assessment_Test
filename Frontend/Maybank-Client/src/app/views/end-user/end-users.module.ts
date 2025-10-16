import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EndUsersComponent } from './end-users.component';
import { EndUsersAddEditComponent } from './end-users-add-edit.component';
import { EndUsersRoutingModule } from './end-users-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';
import {FormsModule} from '@angular/forms';
import {AuthUsersComponent} from './auth-users.component';
import {AuthUsersAddEditComponent} from './auth-users-add-edit.component';
import {EndUsersReloadComponent} from './end-users-reload.component';

@NgModule({
  imports: [
    EndUsersRoutingModule,
    NgxPaginationModule,
    CommonModule,
    FormsModule
  ],
  declarations: [ EndUsersComponent, EndUsersAddEditComponent, AuthUsersComponent, AuthUsersAddEditComponent, EndUsersReloadComponent]
})
export class EndUsersModule { }
