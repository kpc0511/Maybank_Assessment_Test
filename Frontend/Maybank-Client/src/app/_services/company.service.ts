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
export class CompanyService {
  constructor(private http: HttpClient) { }

  getAllCompanies(): Observable<any> {
    return this.http.get(AppConstants.COMPANY_API + 'getAllCompanies');
  }

  getTransactionListing(customerId: string, description: string, accountNumbers: String[],
                    page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.TRANSACTION_API + 'search', {
      customerId,
      description,
      accountNumbers,
      page,
      size,
      sort
    }, httpOptions);
  }

  getTransactionById(id: number): Observable<any> {
    return this.http.get(AppConstants.TRANSACTION_API + id);
  }

  saveTransaction(id: number, description: string, version: bigint): Observable<any> {
    return this.http.put(AppConstants.TRANSACTION_API + id, {
      description,
      version
    }, httpOptions);
  }
}
