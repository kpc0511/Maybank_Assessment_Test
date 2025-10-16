import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {RankTypeAddEditComponent} from '../rank/rank-type-add-edit.component';
import {SongsComponent} from './songs.component';
import {CreditsAddEditComponent} from '../credit/credits-add-edit.component';
import {SongsAddEditComponent} from './songs-add-edit.component';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Song'
    },
    children: [
      {
        path: '',
        redirectTo: 'song-listing'
      },
      {
        path: 'song-listing',
        component: SongsComponent,
        data: {
          title: 'Song Listing'
        }
      },
      {
        path: 'add-song',
        component: SongsAddEditComponent,
        data: {
          title: 'Add Song'
        }
      },
      {
        path: 'edit-song/:id',
        component: SongsAddEditComponent,
        data: {
          title: 'Edit Song'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SongRoutingModule {}
