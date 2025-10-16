import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {BranchesComponent} from './branches.component';
import {BranchesAddEditComponent} from './branches-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Branch-Manage'
    },
    children: [
      {
        path: '',
        redirectTo: 'branchManage'
      },
      {
        path: 'branch-list',
        component: BranchesComponent,
        data: {
          title: 'Branch List'
        }
      },
      {
        path: 'add-branch',
        component: BranchesAddEditComponent,
        data: {
          title: 'Add Branch'
        }
      },
      {
        path: 'edit-branch/:id',
        component: BranchesAddEditComponent,
        data: {
          title: 'Edit Branch'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BranchManageRoutingModule {}
