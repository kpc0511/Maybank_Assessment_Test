import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import { navItems } from '../../_nav';
import { AuthService } from '../../_services/auth.service';
import {User} from '../../data-model/user';
import {BehaviorSubject} from 'rxjs';
import {MyINavData} from '../../MyINavData';
import {AppSidebarService} from '../../_services/app-side-bar.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent implements OnInit {
  public navItems$: BehaviorSubject<MyINavData[]> = new BehaviorSubject<Array<MyINavData>>(new Array<MyINavData>());
  public sidebarMinimized = false;
  // public navItems = navItems;
  private user: User;
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  showUserBoard = false;
  displayName?: string;

  constructor(private sidebarService: AppSidebarService,
    private router: Router, private authService: AuthService) { this.authService.user.subscribe(x => this.user = x); }

  ngOnInit() {
    this.isLoggedIn = !!this.user;
    if (this.isLoggedIn) {
      this.showAdminBoard = this.user.roles.includes('ROLE_ADMIN');
      this.showModeratorBoard = this.user.roles.includes('ROLE_MODERATOR');
      this.showUserBoard = this.user.roles.includes('ROLE_USER');
      this.displayName = this.user.displayName;
    }
    this.sidebarService
      .getCurrentUserMenu(this.user)
      .subscribe((items) => {
        this.navItems$.next(items);
        }
      );
  }

  toggleMinimize(e) {
    this.sidebarMinimized = e;
  }

  logout(): void {
    this.authService.logout();
  }

  reloadPage(): void {
    window.location.reload();
  }
}
