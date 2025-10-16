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
export class TermService {
  constructor(private http: HttpClient) { }

  getTermListing(title: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.TERM_API + 'getTermListing', {
      title,
      page,
      size,
      sort
    }, httpOptions);
  }

  getTermById(id: number): Observable<any> {
    return this.http.post(AppConstants.TERM_API + 'getTermById', {
      id
    }, httpOptions);
  }

  deleteTerm(id: bigint): Observable<any> {
    return this.http.post(AppConstants.TERM_API + 'deleteTerm', {
      id
    }, httpOptions);
  }

  saveTerm(id: number, title: string, description: string,
           status: number, createBy: string, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.TERM_API + 'saveTerm', {
      description,
      title,
      status,
      createBy,
      companyId,
      id
    }, httpOptions);
  }
}
