import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { UserModel } from '../../data-model/user.model';
import { first } from 'rxjs/operators';
import { AuthUserService } from '../../_services/auth-user.service';

@Component({
  templateUrl: 'auth-users.component.html'
})
export class AuthUsersComponent implements OnInit {
  user = null;
  page = 1;
  displayName = '';
  provider = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  users: UserModel[] = [];
  currentIndex = -1;
  currentUserModel: UserModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private authUserService: AuthUserService, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.user = this.tokenStorageService.getUser();
    this.retrieveUsers();
  }

  retrieveUsers(): void {
    this.authUserService.getUserListing(this.displayName, this.provider, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        console.log(data);
        const { users, totalItems } = data;
        this.users = users;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveUser(userModel: UserModel, index: number): void {
    this.currentUserModel = userModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveUsers();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveUsers();
  }

  searchDisplayName(): void {
    this.page = 1;
    this.retrieveUsers();
  }

  deleteEndUser(id: bigint) {
    const getUser = this.users.find(x => x.id === id);
    if (!getUser) { return; }
    this.authUserService.deleteAuthUser(getUser.id).pipe(first()).subscribe(
      data => {
        console.log(data);
        this.isDeleteSuccessful = true;
        this.successDeleteMessage = data.message;
        this.ngOnInit();
        this.removeAlert();
      },
      err => {
        console.log(err);
        this.isDeleteFailed = true;
        this.errorDeleteMessage = err;
      }
    );
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isDeleteSuccessful = false;
    }, 3000);
  }

  getImage(imageData: any) {
    if (imageData === null) {
      return 'assets/img/users/default-user.jpg';
    } else {
      const imageBase64 = 'data:image/jpeg;base64,' + imageData.data;
      return imageBase64;
    }
  }

  getGender(input: any) {
    let gender = 'Other';
    switch ( input ) {
      case 0:
        gender = 'Male';
        break;
      case 1:
        gender = 'Female';
        break;
    }
    return gender;
  }
}
