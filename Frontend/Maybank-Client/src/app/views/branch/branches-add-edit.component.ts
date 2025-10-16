import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { TokenStorageService } from '../../_services/token-storage.service';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {BranchModel} from '../../data-model/branch.model';
import {BranchService} from '../../_services/branch.service';
import {TableService} from '../../_services/table.service';
import {TableModel} from '../../data-model/table.model';
import {AuthService} from '../../_services/auth.service';
import {GiftService} from '../../_services/gift.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'branches-add-edit.component.html',
  styleUrls: ['./branches-add-edit.component.css']
})
export class BranchesAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public branchModel: BranchModel = new BranchModel();
  public editBranchId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];
  private selectTableId: bigint[] = [];
  private selectGiftId: bigint[] = [];
  private selectPromotionId: bigint[] = [];

  dropdownSettings: any = {};
  dropdownGiftSettings: any = {};
  dropdownTableList: Array<any> = [];
  dropdownGiftList: Array<any> = [];
  dropdownCompanyList: Array<any> = [];
  selectedTableItems: Array<any> = [];
  selectedGiftItems: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private authService: AuthService,
              private tableService: TableService,
              private giftService: GiftService,
              private branchService: BranchService,
              private imageService: ImageService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editBranchId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editBranchId;
    if (!this.isAddMode) {
      this.branchService.getBranchById(this.editBranchId).subscribe(
        data => {
          this.branchModel = data;
          if (data.company['id'] !== null) {
            this.branchModel.companyId = data.company['id'];
          }
          this.fetchSelectedTableItems(this.branchModel.branchTableList);
          this.fetchSelectedGiftItems(this.branchModel.giftsList);
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

    this.tableService.getAllTable().subscribe(
      data => {
        this.dropdownTableList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );

    this.giftService.getAllGift().subscribe(
      data => {
        this.dropdownGiftList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'tableName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 10,
      allowSearchFilter: true
    };

    this.dropdownGiftSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'giftName',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 10,
      allowSearchFilter: true
    };
  }

  onSubmit(): void {
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.imageService.uploadNew(this.selectedFiles[i], 'branches').subscribe(
          imageData => {
            this.imageId = imageData.id;
              this.branchService.saveBranch(this.editBranchId, this.branchModel.branchName, this.branchModel.address1, this.branchModel.address2,
              this.branchModel.address3, this.branchModel.state, this.branchModel.city, this.branchModel.postcode, this.selectTableId,
              this.selectGiftId, this.selectPromotionId, this.user['displayName'], this.imageId, this.branchModel.companyId).subscribe(
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
      this.branchService.saveBranch(this.editBranchId, this.branchModel.branchName, this.branchModel.address1, this.branchModel.address2,
        this.branchModel.address3, this.branchModel.state, this.branchModel.city, this.branchModel.postcode, this.selectTableId,
        this.selectGiftId, this.selectPromotionId, this.user['displayName'], null, this.branchModel.companyId).subscribe(
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
    this.router.navigate(['/branchManage/branch-list']);
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

  onItemSelect(item: any) {
    this.selectTableId.push(item.id);
  }
  onItemDeSelect(item: any) {
    const index = this.selectTableId.findIndex(function(o) {
      return o === item.id;
    });
    if (index !== -1) {
      this.selectTableId.splice(index, 1);
    }
  }
  onSelectAll(items: any) {
    this.selectTableId = [];
    for (let i = 0; i < items.length; i++) {
      this.selectTableId.push(items[i].id);
    }
  }
  fetchSelectedTableItems(items: any) {
    this.selectedTableItems = [];
    for (let i = 0; i < items.length; i++) {
      this.selectedTableItems.push({id: items[i].id, tableName: items[i].tableName});
    }
  }

  onSelectGift(item: any) {
    this.selectGiftId.push(item.id);
  }
  onDeSelectGift(item: any) {
    const index = this.selectGiftId.findIndex(function(o) {
      return o === item.id;
    });
    if (index !== -1) {
      this.selectGiftId.splice(index, 1);
    }
  }
  onSelectAllGift(items: any) {
    this.selectGiftId = [];
    for (let i = 0; i < items.length; i++) {
      this.selectGiftId.push(items[i].id);
    }
  }
  fetchSelectedGiftItems(items: any) {
    this.selectedGiftItems = [];
    for (let i = 0; i < items.length; i++) {
      this.selectedGiftItems.push({id: items[i].id, giftName: items[i].giftName});
    }
  }
}
