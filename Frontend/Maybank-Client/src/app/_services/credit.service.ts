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
export class CreditService {
  constructor(private http: HttpClient) { }

  getCreditListing(creditTitle: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.CREDIT_API + 'getSongCreditListing', {
      creditTitle,
      page,
      size,
      sort
    }, httpOptions);
  }

  getSongCreditById(id: number): Observable<any> {
    return this.http.post(AppConstants.CREDIT_API + 'getSongCreditById', {
      id
    }, httpOptions);
  }

  deleteSongCredit(id: bigint): Observable<any> {
    return this.http.post(AppConstants.CREDIT_API + 'deleteSongCredit', {
      id
    }, httpOptions);
  }

  saveSongCredit(id: number, creditTitle: string, creditName: string, description: string,
                 deductCredit: number, createBy: string, rankDetailId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.CREDIT_API + 'saveSongCredit', {
      creditTitle,
      creditName,
      description,
      deductCredit,
      rankDetailId,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
