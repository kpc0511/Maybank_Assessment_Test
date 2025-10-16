import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { EndUserService } from '../../_services/end-user.service';
import { UserModel } from '../../data-model/user.model';
import { first } from 'rxjs/operators';
import {RankModel} from '../../data-model/rank.model';
import {RankService} from '../../_services/rank.service';
import {AuthService} from '../../_services/auth.service';

@Component({
  templateUrl: 'rank-types.component.html'
})
export class RankTypesComponent implements OnInit {
  user = null;
  page = 1;
  rankingName = '';
  provider = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  rankings: RankModel[] = [];
  currentIndex = -1;
  currentRankModel: RankModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private rankService: RankService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveRanks();
  }

  retrieveRanks(): void {
    this.rankService.getRankListing(this.rankingName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        console.log(data);
        const { rankings, totalItems } = data;
        this.rankings = rankings;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveRank(rankModel: RankModel, index: number): void {
    this.currentRankModel = rankModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveRanks();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveRanks();
  }

  searchRankingName(): void {
    this.page = 1;
    this.retrieveRanks();
  }

  deleteRank(id: bigint) {
    const getRank = this.rankings.find(x => x.id === id);
    if (!getRank) { return; }
    this.rankService.deleteRank(getRank.id).pipe(first()).subscribe(
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
}
