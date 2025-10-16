import {Component, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {TokenStorageService} from '../../_services/token-storage.service';
import {EndUserService} from '../../_services/end-user.service';
import {UserModel} from '../../data-model/user.model';
import {Router, ActivatedRoute} from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'end-users-reload.component.html',
  styleUrls: ['./end-users-reload.component.css']
})
export class EndUsersReloadComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public userModel: UserModel = new UserModel();
  public reloadUserId!: number;

  constructor(@Inject(DOCUMENT) private _document: any,
              private authService: AuthService,
              private endUserService: EndUserService,
              private imageService: ImageService,
              private tokenStorageService: TokenStorageService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.reloadUserId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.endUserService.getUserById(this.reloadUserId).subscribe(
      data => {
        this.userModel = data;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(): void {
    this.endUserService.reloadCredit(this.reloadUserId, this.userModel.credit,
      this.user['displayName'], this.user['displayName'], this.userModel.description).subscribe(
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

  removeAlert(): void {
    setTimeout(() => {
      this.isSuccessful = false;
    }, 3000);
  }

  back2List() {
    this.router.navigate(['/userManage/end-user-list']);
  }
}
