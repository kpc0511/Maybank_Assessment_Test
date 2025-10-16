import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { EndUserService } from '../../_services/end-user.service';
import { UserModel } from '../../data-model/user.model';
import { first } from 'rxjs/operators';
import {RankModel} from '../../data-model/rank.model';
import {RankService} from '../../_services/rank.service';
import {AuthService} from '../../_services/auth.service';
import {RankDetailModel} from '../../data-model/rank-detail.model';

@Component({
  templateUrl: 'rank-details.component.html'
})
export class RankDetailsComponent implements OnInit {
  user = null;
  page = 1;
  levelName = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  rankingDetails: RankDetailModel[] = [];
  currentIndex = -1;
  currentRankDetailModel: RankDetailModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private rankService: RankService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveRankDetails();
  }

  retrieveRankDetails(): void {
    this.rankService.getRankDetailListing(this.levelName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        console.log(data);
        const { rankingDetails, totalItems } = data;
        this.rankingDetails = rankingDetails;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveRankDetail(rankDetailModel: RankDetailModel, index: number): void {
    this.currentRankDetailModel = rankDetailModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveRankDetails();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveRankDetails();
  }

  searchLevelName(): void {
    this.page = 1;
    this.retrieveRankDetails();
  }

  deleteRankDetail(id: bigint) {
    const getRankDetail = this.rankingDetails.find(x => x.id === id);
    if (!getRankDetail) { return; }
    this.rankService.deleteRankDetail(getRankDetail.id).pipe(first()).subscribe(
      data => {
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
}
