import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {CompanyModel} from '../../data-model/company.model';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'companies-add-edit.component.html',
  styleUrls: ['./companies-add-edit.component.css']
})
export class CompaniesAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public companyModel: CompanyModel = new CompanyModel();
  public editCompanyId!: number;
  public isAddMode!: boolean;

  constructor(@Inject(DOCUMENT) private _document: any,
              private authService: AuthService,
              private companyService: CompanyService,
              private imageService: ImageService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editCompanyId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editCompanyId;
    if (!this.isAddMode) {
      this.companyService.getTransactionById(this.editCompanyId).subscribe(
        data => {
          this.companyModel = data.data;
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }
  }

  onSubmit(): void {
      this.companyService.saveTransaction(this.editCompanyId, this.companyModel.description, this.companyModel.version).subscribe(
        data => {
          this.isSuccessful = true;
          this.isFailed = false;
          this.successMessage = data.message;
          this.companyService.getTransactionById(this.editCompanyId).subscribe(
            updatedData => {
              this.companyModel = updatedData.data; // Update the model with the latest data
            },
            err => {
              this.errorMessage = err.error.message;
            }
          );
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
    this.router.navigate(['/companyManage/transaction-list']);
  }
}
