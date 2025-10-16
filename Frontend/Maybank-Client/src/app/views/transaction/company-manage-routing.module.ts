import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CompaniesComponent} from './companies.component';
import {CompaniesAddEditComponent} from './companies-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Transaction-Manage'
    },
    children: [
      {
        path: '',
        redirectTo: 'companyManage'
      },
      {
        path: 'transaction-list',
        component: CompaniesComponent,
        data: {
          title: 'Transaction List'
        }
      },
      {
        path: 'add-transaction',
        component: CompaniesAddEditComponent,
        data: {
          title: 'Add Company'
        }
      },
      {
        path: 'edit-transaction/:id',
        component: CompaniesAddEditComponent,
        data: {
          title: 'Edit Company'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompanyManageRoutingModule {}
