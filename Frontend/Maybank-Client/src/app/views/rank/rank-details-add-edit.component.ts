import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {AuthService} from '../../_services/auth.service';
import {RankModel} from '../../data-model/rank.model';
import {RankService} from '../../_services/rank.service';
import {RankDetailModel} from '../../data-model/rank-detail.model';
import {ImageService} from '../../_services/image.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'rank-details-add-edit.component.html',
  styleUrls: ['./rank-details-add-edit.component.css']
})
export class RankDetailsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public rankDetailModel: RankDetailModel = new RankDetailModel();
  public editRankDetailId!: number;
  public isAddMode!: boolean;
  public rankTypes: Array<any> = [];
  public imageId?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];

  dropdownRankTypeList: Array<any> = [];
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private rankService: RankService,
              private authService: AuthService,
              private imageService: ImageService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editRankDetailId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editRankDetailId;
    if (!this.isAddMode) {
      this.rankService.getRankDetailById(this.editRankDetailId).subscribe(
        data => {
          console.log(data);
          this.rankDetailModel = data;
          if (data.ranking['id'] !== null) {
            this.rankDetailModel.rankTypeId = data.ranking['id'];
          }
          if (data.company['id'] !== null) {
            this.rankDetailModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }

    this.rankService.getAllRanks().subscribe(
      data => {
        this.dropdownRankTypeList = data;
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

    this.rankTypes = [
      { id: 'send_gift_ranking', name: 'Send Gift Ranking' },
      { id: 'receive_gift_ranking', name: 'Receive Gift Ranking' }
    ];
  }

  onSubmit(): void {
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.imageService.uploadNew(this.selectedFiles[i], 'users').subscribe(
          imageData => {
            this.imageId = imageData.id;
            this.rankService.saveRankDetail(this.editRankDetailId, this.rankDetailModel.levelName, this.rankDetailModel.levelTitle,
              this.rankDetailModel.valueMin, this.rankDetailModel.valueMax, this.user['displayName'], this.imageId, this.rankDetailModel.rankTypeId, this.rankDetailModel.companyId).subscribe(
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
      this.rankService.saveRankDetail(this.editRankDetailId, this.rankDetailModel.levelName, this.rankDetailModel.levelTitle,
        this.rankDetailModel.valueMin, this.rankDetailModel.valueMax, this.user['displayName'], null, this.rankDetailModel.rankTypeId, this.rankDetailModel.companyId).subscribe(
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

  back2List() {
    this.router.navigate(['/rankManage/rank-detail-list']);
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
