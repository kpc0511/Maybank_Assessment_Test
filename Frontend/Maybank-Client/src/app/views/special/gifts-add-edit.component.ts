import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {GiftModel} from '../../data-model/gift.model';
import {GiftService} from '../../_services/gift.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'gifts-add-edit.component.html',
  styleUrls: ['./gifts-add-edit.component.css']
})
export class GiftsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public giftModel: GiftModel = new GiftModel();
  public editGiftId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];

  dropdownSettings: any = {};
  dropdownTableList: Array<any> = [];
  selectedTableItems: Array<any> = [];
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private giftService: GiftService,
              private authService: AuthService,
              private imageService: ImageService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editGiftId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editGiftId;
    if (!this.isAddMode) {
      this.giftService.getGiftById(this.editGiftId).subscribe(
        data => {
          this.giftModel = data;
          if (data.company['id'] !== null) {
            this.giftModel.companyId = data.company['id'];
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
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.imageService.uploadNew(this.selectedFiles[i], 'branches').subscribe(
          imageData => {
            this.imageId = imageData.id;
              this.giftService.saveGift(this.editGiftId, this.giftModel.giftName, this.giftModel.title, this.giftModel.requiredValues,
              this.giftModel.requiredPoints, this.user['displayName'], this.imageId, this.giftModel.companyId).subscribe(
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
      this.giftService.saveGift(this.editGiftId, this.giftModel.giftName, this.giftModel.title, this.giftModel.requiredValues,
        this.giftModel.requiredPoints, this.user['displayName'], null, this.giftModel.companyId).subscribe(
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
    this.router.navigate(['/special/gift-listing']);
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
