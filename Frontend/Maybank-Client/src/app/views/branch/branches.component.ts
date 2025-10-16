import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { first } from 'rxjs/operators';
import {BranchModel} from '../../data-model/branch.model';
import {BranchService} from '../../_services/branch.service';
import {CreditService} from '../../_services/credit.service';
import {AuthService} from '../../_services/auth.service';

@Component({
  templateUrl: 'branches.component.html'
})
export class BranchesComponent implements OnInit {
  user = null;
  page = 1;
  branchName = '';
  provider = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  branches: BranchModel[] = [];
  currentIndex = -1;
  currentBranchModel: BranchModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private branchService: BranchService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveBranches();
  }

  retrieveBranches(): void {
    this.branchService.getBranchListing(this.branchName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { branches, totalItems } = data;
        this.branches = branches;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveBranch(branchModel: BranchModel, index: number): void {
    this.currentBranchModel = branchModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveBranches();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveBranches();
  }

  searchBranchName(): void {
    this.page = 1;
    this.retrieveBranches();
  }

  deleteBranch(id: bigint) {
    const getBranch = this.branches.find(x => x.id === id);
    if (!getBranch) { return; }
    this.branchService.deleteBranch(getBranch.id).pipe(first()).subscribe(
      data => {
        console.log(data);
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
