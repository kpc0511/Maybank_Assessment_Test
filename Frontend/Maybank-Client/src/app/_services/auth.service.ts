import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import { AppConstants } from '../common/app.constants';
import {catchError, map, tap} from 'rxjs/operators';
import {User} from '../data-model/user';
import {Router} from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;

  constructor(private router: Router, private http: HttpClient) {
    this.userSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
    this.user = this.userSubject.asObservable();
  }

  public get userValue(): User {
    return this.userSubject.value;
  }

  login(data: string): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + 'signIn', {
      data
    }, httpOptions).pipe(map(returnData => {
      localStorage.setItem('user', JSON.stringify(returnData));
      this.userSubject.next(returnData);
      return returnData;
    }));
  }

  register(displayName: string, email: string, password: string, matchingPassword: string,
     mobileNumber: string, role: String[]): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + 'signUp', {
      displayName,
      email,
      password,
      matchingPassword,
      mobileNumber,
      socialProvider: 'LOCAL'
    }, httpOptions);
  }

  forgetPassword(email: string): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + 'forget-password', {
      email
    }, httpOptions);
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + 'reset-password', {
      token,
      newPassword
    }, httpOptions);
  }

  logout(): Observable<any> {
    return this.http.post(AppConstants.AUTH_API + 'logout', {}, httpOptions).pipe(
      tap(() => {
        // After successfully logging out from the server
        this.localLogout();
      }),
      catchError(error => {
        console.error('Logout failed', error); // Handle error if needed
        return throwError(error);
      })
    );
  }

  localLogout() {
    // Clear local storage and navigate to login
    localStorage.removeItem('user');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }
}
