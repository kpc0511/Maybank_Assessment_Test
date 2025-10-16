import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {TablesComponent} from '../branch-table/tables.component';
import {EndUsersAddEditComponent} from '../end-user/end-users-add-edit.component';
import {TableAddEditComponent} from './table-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Table-Manage'
    },
    children: [
      {
        path: '',
        redirectTo: 'tableManage'
      },
      {
        path: 'table-list',
        component: TablesComponent,
        data: {
          title: 'Table List'
        }
      },
      {
        path: 'add-table',
        component: TableAddEditComponent,
        data: {
          title: 'Add Table'
        }
      },
      {
        path: 'edit-table/:id',
        component: TableAddEditComponent,
        data: {
          title: 'Edit Table'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TableManageRoutingModule {}
