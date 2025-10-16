import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {AuthService} from '../../_services/auth.service';
import {RankModel} from '../../data-model/rank.model';
import {RankService} from '../../_services/rank.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'rank-type-add-edit.component.html',
  styleUrls: ['./rank-type-add-edit.component.css']
})
export class RankTypeAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public rankModel: RankModel = new RankModel();
  public editRankTypeId!: number;
  public isAddMode!: boolean;
  public rankTypes: Array<any> = [];

  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private rankService: RankService,
              private authService: AuthService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editRankTypeId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editRankTypeId;
    if (!this.isAddMode) {
      this.rankService.getRankById(this.editRankTypeId).subscribe(
        data => {
          this.rankModel = data;
          if (data.company['id'] !== null) {
            this.rankModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }
    this.rankTypes = [
      { id: 'send_gift_ranking', name: 'Send Gift Ranking' },
      { id: 'receive_gift_ranking', name: 'Receive Gift Ranking' }
    ];
    this.companyService.getAllCompanies().subscribe(
      data => {
        this.dropdownCompanyList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(): void {
      this.rankService.saveRank(this.editRankTypeId, this.rankModel.rankingName, this.rankModel.rankingType,
        this.user['displayName'], this.rankModel.companyId).subscribe(
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
    this.router.navigate(['/rankManage/rank-type-list']);
  }
}
