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
export class TableService {
  constructor(private http: HttpClient) { }

  getAllTable(): Observable<any> {
    return this.http.get(AppConstants.TABLE_API + 'getAllTables');
  }

  getTableListing(tableName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.TABLE_API + 'getTableListing', {
      tableName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getTableById(id: number): Observable<any> {
    return this.http.post(AppConstants.TABLE_API + 'getTableById', {
      id
    }, httpOptions);
  }

  saveTable(id: number, tableName: string, tableType: string, numberOfSeat: number,
             createBy: string, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.TABLE_API + 'saveTable', {
      tableName,
      tableType,
      numberOfSeat,
      companyId,
      createBy,
      id
    }, httpOptions);
  }

  deleteTable(id: bigint): Observable<any> {
    return this.http.post(AppConstants.TABLE_API + 'deleteTable', {
      id
    }, httpOptions);
  }
}
