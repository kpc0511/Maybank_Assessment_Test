import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RankTypesComponent} from './rank-types.component';
import {RankTypeAddEditComponent} from './rank-type-add-edit.component';
import {RankDetailsComponent} from './rank-details.component';
import {RankDetailsAddEditComponent} from './rank-details-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Rank-Manage'
    },
    children: [
      {
        path: '',
        redirectTo: 'rankManage'
      },
      {
        path: 'rank-type-list',
        component: RankTypesComponent,
        data: {
          title: 'Rank-Type List'
        }
      },
      {
        path: 'add-rank-type',
        component: RankTypeAddEditComponent,
        data: {
          title: 'Add Rank-Type'
        }
      },
      {
        path: 'edit-rank-type/:id',
        component: RankTypeAddEditComponent,
        data: {
          title: 'Edit Rank-Type'
        }
      },
      {
        path: 'rank-detail-list',
        component: RankDetailsComponent,
        data: {
          title: 'Rank-Detail List'
        }
      },
      {
        path: 'add-rank-detail',
        component: RankDetailsAddEditComponent,
        data: {
          title: 'Add Rank-Detail'
        }
      },
      {
        path: 'edit-rank-detail/:id',
        component: RankDetailsAddEditComponent,
        data: {
          title: 'Edit Rank-Detail'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RanksRoutingModule {}
