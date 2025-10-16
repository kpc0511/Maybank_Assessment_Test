import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AppConstants} from '../common/app.constants';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  constructor(private http: HttpClient) { }

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    const req = new HttpRequest('POST', AppConstants.IMAGE_API + 'upload', formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  uploadNew(file: File, imageTable: string): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('imageTable', imageTable);
    return this.http.post(AppConstants.IMAGE_API + 'upload', formData);
  }

  getFiles(): Observable<any> {
    return this.http.get(AppConstants.IMAGE_API + 'files');
  }

  getImage(imageName: string): Observable<any> {
    return this.http.get(AppConstants.IMAGE_API + 'files/' + imageName);
  }
}
