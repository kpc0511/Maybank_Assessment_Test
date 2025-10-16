import {Component, OnInit} from '@angular/core';
import { first } from 'rxjs/operators';
import {AuthService} from '../../_services/auth.service';
import {PromotionModel} from '../../data-model/promotion.model';
import {PromotionService} from '../../_services/promotion.service';

@Component({
  templateUrl: 'promotions.component.html'
})
export class PromotionsComponent implements OnInit {
  user = null;
  page = 1;
  searchName = '';
  promotionType = 'ALL';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  promotions: PromotionModel[] = [];
  currentIndex = -1;
  currentPromotionModel: PromotionModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';
  public promotionTypes: Array<any> = [];

  constructor(private promotionService: PromotionService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrievePromotions();

    this.promotionTypes = [
      { id: 'PROMOTION_TYPE', name: 'Promotion' },
      { id: 'ANNOUNCEMENT_TYPE', name: 'Announcement' }
    ];
  }

  retrievePromotions(): void {
    this.promotionService.getPromotionListing(this.searchName, this.promotionType,
      this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { promotions, totalItems } = data;
        this.promotions = promotions;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActivePromotion(promotionModel: PromotionModel, index: number): void {
    this.currentPromotionModel = promotionModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrievePromotions();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrievePromotions();
  }

  searchPromotion(): void {
    this.page = 1;
    this.retrievePromotions();
  }

  deletePromotion(id: bigint) {
    const getPromotion = this.promotions.find(x => x.id === id);
    if (!getPromotion) { return; }
    this.promotionService.deletePromotion(getPromotion.id).pipe(first()).subscribe(
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
