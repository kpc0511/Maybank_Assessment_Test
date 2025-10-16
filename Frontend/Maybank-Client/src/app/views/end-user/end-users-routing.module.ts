import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthUsersComponent } from './auth-users.component';
import { EndUsersComponent } from './end-users.component';
import { EndUsersAddEditComponent } from './end-users-add-edit.component';
import {AuthUsersAddEditComponent} from './auth-users-add-edit.component';
import {EndUsersReloadComponent} from './end-users-reload.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'User-Manage'
    },
    children: [
      {
        path: '',
        redirectTo: 'userManage'
      },
      {
        path: 'end-user-list',
        component: EndUsersComponent,
        data: {
          title: 'End-User List'
        }
      },
      {
        path: 'add-end-user',
        component: EndUsersAddEditComponent,
        data: {
          title: 'Add End-Users'
        }
      },
      {
        path: 'edit-end-users/:id',
        component: EndUsersAddEditComponent,
        data: {
          title: 'Edit End-Users'
        }
      },
      {
        path: 'reload-end-users/:id',
        component: EndUsersReloadComponent,
        data: {
          title: 'Reload End-Users'
        }
      },
      {
        path: 'auth-user-list',
        component: AuthUsersComponent,
        data: {
          title: 'Auth-User List'
        }
      },
      {
        path: 'add-auth-user',
        component: AuthUsersAddEditComponent,
        data: {
          title: 'Add Auth-Users'
        }
      },
      {
        path: 'edit-auth-users/:id',
        component: AuthUsersAddEditComponent,
        data: {
          title: 'Edit Auth-Users'
        }
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EndUsersRoutingModule {}
