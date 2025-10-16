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
export class AuthUserService {
  constructor(private http: HttpClient) { }

  getUserListing(displayName: string, provider: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'getAuthUserListing', {
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


  saveAuthUser(id: number, email: string, displayName: string, enabled: boolean, password: string, phone: string, gender: string,
              // tslint:disable-next-line:max-line-length
              dob: string, personalDescription: string, currentJob: string, remark: string, role: String[], createBy: string, fileId: bigint): Observable<any> {
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
      createBy
    }, httpOptions);
  }

  deleteAuthUser(id: bigint): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'deleteUser', {
      id
    }, httpOptions);
  }
}
