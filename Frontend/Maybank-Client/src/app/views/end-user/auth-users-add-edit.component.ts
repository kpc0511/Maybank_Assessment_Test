import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { TokenStorageService } from '../../_services/token-storage.service';
import { AuthUserService } from '../../_services/auth-user.service';
import { UserModel } from '../../data-model/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';

@Component({
  templateUrl: 'auth-users-add-edit.component.html',
  styleUrls: ['./auth-users-add-edit.component.css']
})
export class AuthUsersAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  private roles = [
    {id: 'ROLE_ADMIN', name: 'Admin', value: 'admin', checked: false},
    {id: 'ROLE_MODERATOR', name: 'Moderator', value: 'mod', checked: false},
    {id: 'ROLE_USER', name: 'User', value: 'user', checked: false}
  ];
  public checkedRoles = [];
  public  userModel: UserModel = new UserModel();
  public editUserId!: number;
  public isAddMode!: boolean;
  public imageid?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private authService: AuthService,
              private authUserService: AuthUserService,
              private imageService: ImageService,
              private tokenStorageService: TokenStorageService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editUserId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editUserId;
    if (!this.isAddMode) {
      this.authUserService.getUserById(this.editUserId).subscribe(
        data => {
          this.roles.forEach(function(x) {
            data.roles.forEach(function(y) {
              if (x.id === y.name) {
                x.checked = true;
              }
            });
          });
          this.userModel = data;
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }
  }

  onSubmit(): void {
    this.fetchSelectedItems();
    console.log(this.selectedFiles);
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.imageService.uploadNew(this.selectedFiles[i], 'users').subscribe(
          imageData => {
            this.imageid = imageData.id;
            this.authUserService.saveAuthUser(this.editUserId, this.userModel.email, this.userModel.displayName, this.userModel.enabled,
              this.userModel.password, this.userModel.phone, this.userModel.gender, this.userModel.dob, this.userModel.personalDescription,
              this.userModel.currentJob, this.userModel.remark, this.checkedRoles, this.user['displayName'], this.imageid).subscribe(
              data => {
                this.isSuccessful = true;
                this.isFailed = false;
                this.successMessage = data.message;
                this.removeAlert();
              },
              err => {
                this.errorMessage = err.error.message;
                this.isFailed = true;
              }
            );
          },
          err => {
            this.errorMessage = err.error.message;
          }
        );
      }
    } else {
      this.authUserService.saveAuthUser(this.editUserId, this.userModel.email, this.userModel.displayName, this.userModel.enabled,
        this.userModel.password, this.userModel.phone, this.userModel.gender, this.userModel.dob, this.userModel.personalDescription,
        this.userModel.currentJob, this.userModel.remark, this.checkedRoles, this.user['displayName'], null).subscribe(
        data => {
          this.isSuccessful = true;
          this.isFailed = false;
          this.successMessage = data.message;
          this.removeAlert();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
    }
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isSuccessful = false;
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

  back2List() {
    this.router.navigate(['/userManage/auth-user-list']);
  }

  selectFiles(event: any): void {
    this.selectedFiles = event.target.files;

    this.previews = [];
    if (this.selectedFiles && this.selectedFiles[0]) {
      const numberOfFiles = this.selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          this.previews.push(e.target.result);
        };
        reader.readAsDataURL(this.selectedFiles[i]);
      }
    }
  }

}
