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
export class EndUserService {
  constructor(private http: HttpClient) { }

  getUserListing(displayName: string, provider: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'getEndUserListing', {
      displayName,
      provider,
      page,
      size,
      sort
    }, httpOptions);
  }

  getUserById(id: number): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'getUserById', {
      id
    }, httpOptions);
  }


  saveEndUser(id: number, email: string, displayName: string, enabled: boolean, password: string, phone: string, gender: string,
              // tslint:disable-next-line:max-line-length
              dob: string, personalDescription: string, currentJob: string, remark: string, role: String[], createBy: string,
              fileId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'saveUser', {
      email,
      displayName,
      enabled,
      password,
      phone,
      gender,
      dob,
      personalDescription,
      currentJob,
      remark,
      role,
      id,
      fileId,
      companyId,
      createBy
    }, httpOptions);
  }

  deleteEndUser(id: bigint): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'deleteUser', {
      id
    }, httpOptions);
  }

  reloadCredit(id: number, reloadAmount: number, createBy: string, updateBy: string, description: string): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'reloadCredit', {
      id,
      reloadAmount,
      createBy,
      updateBy,
      description
    }, httpOptions);
  }
}
