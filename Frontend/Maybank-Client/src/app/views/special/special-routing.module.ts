import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {GiftsComponent} from './gifts.component';
import {PromotionsComponent} from './promotions.component';
import {GiftsAddEditComponent} from './gifts-add-edit.component';
import {BranchesAddEditComponent} from '../branch/branches-add-edit.component';
import {PromotionsAddEditComponent} from './promotions-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Gift & Promotion'
    },
    children: [
      {
        path: '',
        redirectTo: 'gift-listing'
      },
      {
        path: 'gift-listing',
        component: GiftsComponent,
        data: {
          title: 'Gift'
        }
      },
      {
        path: 'add-gift',
        component: GiftsAddEditComponent,
        data: {
          title: 'Add Gift'
        }
      },
      {
        path: 'edit-gift/:id',
        component: GiftsAddEditComponent,
        data: {
          title: 'Edit Gift'
        }
      },
      {
        path: 'promotion-listing',
        component: PromotionsComponent,
        data: {
          title: 'Promotion'
        }
      },
      {
        path: 'add-promotion',
        component: PromotionsAddEditComponent,
        data: {
          title: 'Add Promotion'
        }
      },
      {
        path: 'edit-promotion/:id',
        component: PromotionsAddEditComponent,
        data: {
          title: 'Edit Promotion'
        }
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SpecialRoutingModule {}
