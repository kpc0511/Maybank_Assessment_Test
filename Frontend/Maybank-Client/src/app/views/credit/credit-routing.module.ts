import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {CreditsComponent} from './credits.component';
import {RankTypeAddEditComponent} from '../rank/rank-type-add-edit.component';
import {CreditsAddEditComponent} from './credits-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Credit Song'
    },
    children: [
      {
        path: '',
        redirectTo: 'credit-listing'
      },
      {
        path: 'credit-listing',
        component: CreditsComponent,
        data: {
          title: 'Credit Song'
        }
      },
      {
        path: 'add-credit',
        component: CreditsAddEditComponent,
        data: {
          title: 'Add Credit Song'
        }
      },
      {
        path: 'edit-credit/:id',
        component: CreditsAddEditComponent,
        data: {
          title: 'Edit Credit Song'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreditRoutingModule {}
