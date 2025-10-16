import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {MyINavData} from '../MyINavData';
import {DomSanitizer} from '@angular/platform-browser';
import {User} from '../data-model/user';
import {share, tap} from 'rxjs/operators';
import {navItems} from '../_nav';
import {TypeOfPermissionRoles} from '../common/app.enum';

@Injectable({
  providedIn: 'root'
})
export class AppSidebarService {
  private menuItems$: BehaviorSubject<MyINavData[]> = new BehaviorSubject<MyINavData[]>(new Array<MyINavData>());

  constructor(private sanitizer: DomSanitizer) {}

  public getCurrentUserMenu = (user: User) =>  {
    let items: MyINavData[] =  navItems.filter( x => x.permissions == null);
    console.log(user);
    console.log(items);
    if (user.id !== undefined && user.roles.length > 0) {
      console.log(user.roles);
      const itemsRequiringPermissions: MyINavData[] =  navItems.filter(x => x.permissions != null);

      const allItems: MyINavData[] = navItems;
      if (user.roles.includes(TypeOfPermissionRoles.roleSuperAdmin)) {
        items = allItems;
      } else {
        itemsRequiringPermissions.filter(item => {
          let isAuthorized;
          console.log(item);
          item.permissions.forEach((neededPermission) => {
            isAuthorized = user.roles.filter((x) => x === neededPermission).length > 0 ? true : false;
          });
          if (isAuthorized) {
            items.push(item);
          }
        });
      }
    }

    // items.filter(item => {
    //   let isAuthorized = false;
    //
    //   for (var i = 0; i < arrayLength; i++) {
    //     console.log(myStringArray[i]);
    //     //Do something
    //   }
    // });

    return of(items).pipe(
      tap(() => { this.menuItems$.next(
        items.sort((a, b) => (a.displayOrder < b.displayOrder ? -1 : 1))
      ); }),
      share()
    ) as BehaviorSubject<MyINavData[]>;
  }
}
