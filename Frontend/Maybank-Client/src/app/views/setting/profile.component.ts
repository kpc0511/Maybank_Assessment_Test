import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { TokenStorageService } from '../../_services/token-storage.service';
import { ProfileService } from '../../_services/profile.service';
import { ProfileModel } from '../../data-model/profile.model';
import {AuthService} from '../../_services/auth.service';
import {User} from '../../data-model/user';

@Component({
  templateUrl: 'profile.component.html'
})
export class ProfileComponent implements OnInit {
  form: any = {
    firstName: null,
    lastName: null,
    mobileNumber: null
  };
  user: User;
  getProfiled = false;
  getProfiledFailed = false;
  isUpdateSuccessful = false;
  isUpdateFailed = false;
  errorMessage = '';
  successMessage = '';
  roles = [
    {id: 'ROLE_USER', name: 'User', value: 'user', checked: false},
    {id: 'ROLE_MODERATOR', name: 'Mod', value: 'mod', checked: false},
    {id: 'ROLE_ADMIN', name: 'Admin', value: 'admin', checked: false}
  ];
  checkedRoles = [];
  profileModel: ProfileModel = new ProfileModel();

  constructor(@Inject(DOCUMENT) private _document: any,
              private profileService: ProfileService, private authService: AuthService, private tokenStorageService: TokenStorageService) {
    this.user = this.authService.userValue;
  }

  ngOnInit(): void {
    this.profileService.getProfile(this.user.id).subscribe(
      data => {
        this.roles.forEach(function(x) {
          data.roles.forEach(function(y) {
            if (x.id === y.name) {
              x.checked = true;
            }
          });
        });
        this.profileModel = data;
        this.getProfiled = true;
      },
      err => {
        this.errorMessage = err.error.message;
        this.getProfiledFailed = true;
      }
    );
  }

  onSubmit(): void {
    this.fetchSelectedItems();
    this.profileService.saveProfile(this.profileModel.id, this.profileModel.displayName,
      this.profileModel.phone, this.profileModel.gender, this.profileModel.dob,
      this.profileModel.personalDescription, this.profileModel.currentJob, this.profileModel.remark,
      this.profileModel.enabled, this.checkedRoles).subscribe(
      data => {
        this.isUpdateSuccessful = true;
        this.isUpdateFailed = false;
        this.successMessage = data.message;
        this.removeAlert();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isUpdateFailed = true;
      }
    );
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isUpdateSuccessful = false;
    }, 3000);
  }

  fetchSelectedItems() {
    this.checkedRoles = [];
    this.roles.filter((value, index) => {
      if (value.checked) {
        this.checkedRoles.push(value.value);
      }
    });
  }
}
