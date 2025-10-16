import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';

import { P404Component } from './views/error/404.component';
import { P500Component } from './views/error/500.component';
import { LoginComponent } from './views/login/login.component';
import { RegisterComponent } from './views/register/register.component';
import {ForgetPasswordComponent} from './views/forget-password/forget-password.component';
import {ResetPasswordComponent} from './views/reset-password/reset-password.component';
import {AuthGuard} from './_helpers/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: '404',
    component: P404Component,
    data: {
      title: 'Page 404'
    }
  },
  {
    path: '500',
    component: P500Component,
    data: {
      title: 'Page 500'
    }
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page'
    }
  },
  {
    path: 'register',
    component: RegisterComponent,
    data: {
      title: 'Register Page'
    }
  },
  {
    path: 'forget-password',
    component: ForgetPasswordComponent,
    data: {
      title: 'Forget-Password Page'
    }
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent,
    data: {
      title: 'Reset-Password Page'
    }
  },
  {
    path: '',
    component: DefaultLayoutComponent,
    canActivate: [AuthGuard],
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'song',
        loadChildren: () => import('./views/song/song.module').then(m => m.SongModule)
      },
      {
        path: 'companyManage',
        loadChildren: () => import('./views/transaction/company-manage.module').then(m => m.CompanyManageModule)
      },
      {
        path: 'credit',
        loadChildren: () => import('./views/credit/credit.module').then(m => m.CreditModule)
      },
      {
        path: 'term',
        loadChildren: () => import('./views/term/term.module').then(m => m.TermModule)
      },
      {
        path: 'special',
        loadChildren: () => import('./views/special/special.module').then(m => m.SpecialModule)
      },
      {
        path: 'rankManage',
        loadChildren: () => import('./views/rank/rank.module').then(m => m.RankModule)
      },
      {
        path: 'userManage',
        loadChildren: () => import('./views/end-user/end-users.module').then(m => m.EndUsersModule)
      },
      {
        path: 'branchManage',
        loadChildren: () => import('./views/branch/branch-manage.module').then(m => m.BranchManageModule)
      },
      {
        path: 'tableManage',
        loadChildren: () => import('./views/branch-table/table-manage.module').then(m => m.TableManageModule)
      },
      {
        path: 'base',
        loadChildren: () => import('./views/base/base.module').then(m => m.BaseModule)
      },
      {
        path: 'buttons',
        loadChildren: () => import('./views/buttons/buttons.module').then(m => m.ButtonsModule)
      },
      {
        path: 'charts',
        loadChildren: () => import('./views/chartjs/chartjs.module').then(m => m.ChartJSModule)
      },
      {
        path: 'dashboard',
        loadChildren: () => import('./views/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'icons',
        loadChildren: () => import('./views/icons/icons.module').then(m => m.IconsModule)
      },
      {
        path: 'notifications',
        loadChildren: () => import('./views/notifications/notifications.module').then(m => m.NotificationsModule)
      },
      {
        path: 'theme',
        loadChildren: () => import('./views/theme/theme.module').then(m => m.ThemeModule)
      },
      {
        path: 'widgets',
        loadChildren: () => import('./views/widgets/widgets.module').then(m => m.WidgetsModule)
      },
      {
        path: 'setting',
        loadChildren: () => import('./views/setting/setting.module').then(m => m.SettingModule)
      }
    ]
  },
  { path: '**', component: P404Component }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy', useHash: false }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
