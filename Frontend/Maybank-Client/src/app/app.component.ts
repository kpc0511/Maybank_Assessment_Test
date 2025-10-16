import { Component, OnInit } from '@angular/core';
import {Router, NavigationEnd, Params, ActivatedRoute} from '@angular/router';

import { IconSetService } from '@coreui/icons-angular';
import { freeSet } from '@coreui/icons';

import { AuthService } from './_services/auth.service';
import { TokenStorageService } from './_services/token-storage.service';
import {User} from './data-model/user';

@Component({
  // tslint:disable-next-line
  selector: 'body',
  template: '<router-outlet></router-outlet>',
  providers: [IconSetService],
})
export class AppComponent implements OnInit {
  private user: User;
  isLoggedIn = false;
  showAdmin = false;
  showModerator = false;
  displayName: string;
  title: 'Maybank-Test-Client';

  constructor(
    private router: Router,
    private authService: AuthService,
    private tokenStorageService: TokenStorageService,
    public iconSet: IconSetService
  ) {
    // iconSet singleton
    iconSet.icons = { ...freeSet };
    this.authService.user.subscribe(x => this.user = x);
  }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
    this.isLoggedIn = !!this.user;
    if (this.isLoggedIn) {
      this.showAdmin = this.user.roles.includes('ROLE_ADMIN');
      this.showModerator = this.user.roles.includes('ROLE_MODERATOR');
      this.displayName = this.user.displayName;
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
