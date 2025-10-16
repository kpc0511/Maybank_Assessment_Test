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
export class SongService {
  constructor(private http: HttpClient) { }

  getSongListing(songName: string, page: number, size: number, sort: String[]): Observable<any> {
    return this.http.post(AppConstants.SONG_API + 'getSongListing', {
      songName,
      page,
      size,
      sort
    }, httpOptions);
  }

  getSongById(id: number): Observable<any> {
    return this.http.post(AppConstants.SONG_API + 'getSongById', {
      id
    }, httpOptions);
  }

  deleteSong(id: bigint): Observable<any> {
    return this.http.post(AppConstants.SONG_API + 'deleteSong', {
      id
    }, httpOptions);
  }

  saveSong(id: number, songName: string, songTitle: string, songDuration: number,
           createBy: string, singerId: bigint, companyId: bigint): Observable<any> {
    return this.http.post(AppConstants.SONG_API + 'saveSong', {
      songName,
      songTitle,
      singerId,
      songDuration,
      companyId,
      createBy,
      id
    }, httpOptions);
  }
}
