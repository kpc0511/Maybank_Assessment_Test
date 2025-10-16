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
export class PromotionService {
  constructor(private http: HttpClient) { }

  getPromotionListing(searchName: string, promotionType: string,
                      page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.PROMOTION_API + 'getPromotionListing', {
      searchName,
      promotionType,
      page,
      size,
      sort
    }, httpOptions);
  }

  getPromotionById(id: number): Observable<any> {
    return this.http.post(AppConstants.PROMOTION_API + 'getPromotionById', {
      id
    }, httpOptions);
  }

  deletePromotion(id: bigint): Observable<any> {
    return this.http.post(AppConstants.PROMOTION_API + 'deletePromotion', {
      id
    }, httpOptions);
  }

  savePromotion(id: number, name: string, title: string,
                status: boolean, promotionType: string, createBy: string, fileId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.PROMOTION_API + 'savePromotion', {
      name,
      title,
      status,
      promotionType,
      fileId,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
