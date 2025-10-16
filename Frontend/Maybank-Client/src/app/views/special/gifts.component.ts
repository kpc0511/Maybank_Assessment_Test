import {Component, OnInit} from '@angular/core';
import { first } from 'rxjs/operators';
import {GiftModel} from '../../data-model/gift.model';
import {GiftService} from '../../_services/gift.service';
import {AuthService} from '../../_services/auth.service';

@Component({
  templateUrl: 'gifts.component.html'
})
export class GiftsComponent implements OnInit {
  user = null;
  page = 1;
  giftName = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  gifts: GiftModel[] = [];
  currentIndex = -1;
  currentGiftModel: GiftModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private giftService: GiftService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveGifts();
  }

  retrieveGifts(): void {
    this.giftService.getGiftListing(this.giftName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { gifts, totalItems } = data;
        this.gifts = gifts;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveGift(giftModel: GiftModel, index: number): void {
    this.currentGiftModel = giftModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveGifts();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveGifts();
  }

  searchGiftName(): void {
    this.page = 1;
    this.retrieveGifts();
  }

  deleteGift(id: bigint) {
    const getBranch = this.gifts.find(x => x.id === id);
    if (!getBranch) { return; }
    this.giftService.deleteGift(getBranch.id).pipe(first()).subscribe(
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
