import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {GiftModel} from '../../data-model/gift.model';
import {GiftService} from '../../_services/gift.service';
import {TermModel} from '../../data-model/term.model';
import {TermService} from '../../_services/term.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'terms-add-edit.component.html',
  styleUrls: ['./terms-add-edit.component.css']
})
export class TermsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public termModel: TermModel = new TermModel();
  public editTermId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;

  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private termService: TermService,
              private authService: AuthService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editTermId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editTermId;
    if (!this.isAddMode) {
      this.termService.getTermById(this.editTermId).subscribe(
        data => {
          this.termModel = data;
          if (data.company['id'] !== null) {
            this.termModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }

    this.companyService.getAllCompanies().subscribe(
      data => {
        this.dropdownCompanyList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(): void {
      this.termService.saveTerm(this.editTermId, this.termModel.title, this.termModel.description, this.termModel.status,
        this.user['displayName'], this.termModel.companyId).subscribe(
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
    this.router.navigate(['/term/term-listing']);
  }
}
