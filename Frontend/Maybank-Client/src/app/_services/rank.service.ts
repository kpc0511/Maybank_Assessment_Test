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
export class RankService {
  constructor(private http: HttpClient) { }

  getAllRanks(): Observable<any> {
    return this.http.get(AppConstants.RANK_API + 'getAllRanks');
  }

  getAllRankDetails(): Observable<any> {
    return this.http.get(AppConstants.RANK_API + 'getAllRankDetails');
  }

  getRankListing(rankingName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'getRankListing', {
      rankingName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getRankById(id: number): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'getRankById', {
      id
    }, httpOptions);
  }

  deleteRank(id: bigint): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'deleteRank', {
      id
    }, httpOptions);
  }

  saveRank(id: number, rankingName: string, rankingType: string,
           createBy: string, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'saveRank', {
      rankingName,
      rankingType,
      companyId,
      createBy,
      id
    }, httpOptions);
  }

  getRankDetailListing(levelName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'getRankDetailListing', {
      levelName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getRankDetailById(id: number): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'getRankDetailById', {
      id
    }, httpOptions);
  }

  deleteRankDetail(id: bigint): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'deleteRankDetail', {
      id
    }, httpOptions);
  }

  saveRankDetail(id: number, levelName: string, levelTitle: string, valueMin: number,
                 valueMax: number, createBy: string, fileId: bigint, rankTypeId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.RANK_API + 'saveRankDetail', {
      levelName,
      levelTitle,
      valueMin,
      valueMax,
      fileId,
      rankTypeId,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
