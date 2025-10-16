import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {GiftModel} from '../../data-model/gift.model';
import {GiftService} from '../../_services/gift.service';
import {TermModel} from '../../data-model/term.model';
import {TermService} from '../../_services/term.service';
import {CreditModel} from '../../data-model/credit.model';
import {CreditService} from '../../_services/credit.service';
import {RankService} from '../../_services/rank.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'credits-add-edit.component.html',
  styleUrls: ['./credits-add-edit.component.css']
})
export class CreditsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public creditModel: CreditModel = new CreditModel();
  public editCreditId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;
  public rankDetails: Array<any> = [];

  dropdownRankDetailList: Array<any> = [];
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private creditService: CreditService,
              private rankService: RankService,
              private authService: AuthService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editCreditId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editCreditId;
    if (!this.isAddMode) {
      this.creditService.getSongCreditById(this.editCreditId).subscribe(
        data => {
          this.creditModel = data;
          if (data.rankingDetail['id'] !== null) {
            this.creditModel.rankingDetailId = data.rankingDetail['id'];
          }
          if (data.company['id'] !== null) {
            this.creditModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }

    this.rankService.getAllRankDetails().subscribe(
      data => {
        this.dropdownRankDetailList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );

    this.companyService.getAllCompanies().subscribe(
      data => {
        this.dropdownCompanyList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(): void {
      this.creditService.saveSongCredit(this.editCreditId, this.creditModel.creditTitle, this.creditModel.creditName,
        this.creditModel.description, this.creditModel.deductCredit,
        this.user['displayName'], this.creditModel.rankingDetailId, this.creditModel.companyId).subscribe(
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
    this.router.navigate(['/credit/credit-listing']);
  }
}
