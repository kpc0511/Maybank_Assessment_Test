import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AppConstants} from '../common/app.constants';

const API_PROFILE_URL = 'http://localhost:8080/api-ktv-fun/user/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  constructor(private http: HttpClient) { }

  getProfile(id: bigint): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'getUserById', {
      id
    }, httpOptions);
  }

  saveProfile(id: bigint, displayName: string, phone: string, gender: string,
              dob: string, personalDescription: string, currentJob: string, remark: string,
              enabled: boolean, role: String[]): Observable<any> {
    return this.http.post(AppConstants.ENDUSER_API + 'saveProfile', {
      id,
      displayName,
      phone,
      gender,
      dob,
      personalDescription,
      remark,
      enabled,
      currentJob,
      role
    }, httpOptions);
  }
}
