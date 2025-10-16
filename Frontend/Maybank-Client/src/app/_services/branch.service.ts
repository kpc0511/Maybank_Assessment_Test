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
export class BranchService {
  constructor(private http: HttpClient) { }

  getBranchListing(branchName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.BRANCH_API + 'getBranchListing', {
      branchName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getBranchById(id: number): Observable<any> {
    return this.http.post(AppConstants.BRANCH_API + 'getBranchById', {
      id
    }, httpOptions);
  }

  deleteBranch(id: bigint): Observable<any> {
    return this.http.post(AppConstants.BRANCH_API + 'deleteBranch', {
      id
    }, httpOptions);
  }

  saveBranch(id: number, branchName: string, address1: string, address2: string,
             address3: string, state: string, city: string, postcode: string, branchTableId: bigint[],
             giftId: bigint[], promotionId: bigint[], createBy: string, fileId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.BRANCH_API + 'saveBranch', {
      branchName,
      address1,
      address2,
      address3,
      state,
      city,
      postcode,
      branchTableId,
      fileId,
      giftId,
      promotionId,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
