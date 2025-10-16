import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {ImageService} from '../../_services/image.service';
import {AuthService} from '../../_services/auth.service';
import {PromotionModel} from '../../data-model/promotion.model';
import {PromotionService} from '../../_services/promotion.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'promotions-add-edit.component.html',
  styleUrls: ['./promotions-add-edit.component.css']
})
export class PromotionsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public promotionModel: PromotionModel = new PromotionModel();
  public editPromotionId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];
  public promotionTypes: Array<any> = [];
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private promotionService: PromotionService,
              private authService: AuthService,
              private imageService: ImageService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editPromotionId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editPromotionId;
    if (!this.isAddMode) {
      this.promotionService.getPromotionById(this.editPromotionId).subscribe(
        data => {
          this.promotionModel = data;
          this.promotionModel.name = (this.promotionModel.type === 'PROMOTION_TYPE' ? this.promotionModel.promotionName : this.promotionModel.announcementName);
          this.promotionModel.title = (this.promotionModel.type === 'PROMOTION_TYPE' ? this.promotionModel.promotionTitle : this.promotionModel.announcementTitle);
          this.promotionModel.promotionType = this.promotionModel.type;
          if (data.company['id'] !== null) {
            this.promotionModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }
    this.promotionTypes = [
      { id: 'PROMOTION_TYPE', name: 'Promotion' },
      { id: 'ANNOUNCEMENT_TYPE', name: 'Announcement' }
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
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.imageService.uploadNew(this.selectedFiles[i], 'branches').subscribe(
          imageData => {
            this.imageId = imageData.id;
              this.promotionService.savePromotion(this.editPromotionId, this.promotionModel.name, this.promotionModel.title, this.promotionModel.status,
                this.promotionModel.promotionType, this.user['displayName'], this.imageId, this.promotionModel.companyId).subscribe(
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
      this.promotionService.savePromotion(this.editPromotionId, this.promotionModel.name, this.promotionModel.title, this.promotionModel.status,
        this.promotionModel.promotionType, this.user['displayName'], null, this.promotionModel.companyId).subscribe(
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
    this.router.navigate(['/special/promotion-listing']);
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
