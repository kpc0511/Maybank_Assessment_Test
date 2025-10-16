import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {TermsComponent} from './terms.component';
import {TermsAddEditComponent} from './terms-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Term & Condition'
    },
    children: [
      {
        path: '',
        redirectTo: 'term-listing'
      },
      {
        path: 'term-listing',
        component: TermsComponent,
        data: {
          title: 'Term & Condition'
        }
      },
      {
        path: 'add-term',
        component: TermsAddEditComponent,
        data: {
          title: 'Add Term'
        }
      },
      {
        path: 'edit-term/:id',
        component: TermsAddEditComponent,
        data: {
          title: 'Edit Term'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TermRoutingModule {}
