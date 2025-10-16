import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from '../common/app.constants';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class GiftService {
  constructor(private http: HttpClient) { }

  getAllGift(): Observable<any> {
    return this.http.get(AppConstants.GIFT_API + 'getAllGifts');
  }

  getGiftListing(giftName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.GIFT_API + 'getGiftListing', {
      giftName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getGiftById(id: number): Observable<any> {
    return this.http.post(AppConstants.GIFT_API + 'getGiftById', {
      id
    }, httpOptions);
  }

  deleteGift(id: bigint): Observable<any> {
    return this.http.post(AppConstants.GIFT_API + 'deleteGift', {
      id
    }, httpOptions);
  }

  saveGift(id: number, giftName: string, title: string, requiredValues: number,
           requiredPoints: number, createBy: string, fileId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.GIFT_API + 'saveGift', {
      giftName,
      title,
      requiredValues,
      requiredPoints,
      fileId,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
