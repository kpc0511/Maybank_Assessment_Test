import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { UserService } from '../../_services/user.service';
import { TokenStorageService } from '../../_services/token-storage.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { encrypt, decrypt } from '../../common/encryption.util';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  currentUser: any;

  constructor(private router: Router,
              private authService: AuthService,
              private route: ActivatedRoute,
              private tokenStorage: TokenStorageService,
              private userService: UserService) { }

  ngOnInit(): void {
    const token: string = this.route.snapshot.queryParamMap.get('token');
    const error: string = this.route.snapshot.queryParamMap.get('error');
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.currentUser = this.tokenStorage.getUser();
      this.roles = this.tokenStorage.getUser().roles;
    } else if (token) {
      this.tokenStorage.saveToken(token);
      this.userService.getCurrentUser().subscribe(
        data => {
          this.login(data);
        },
        err => {
          this.errorMessage = err.error.message;
          this.isLoginFailed = true;
        }
      );
    } else if (error) {
      this.errorMessage = error;
      this.isLoginFailed = true;
    }
  }

  onSubmit(): void {
    const { username, password } = this.form;
    const encryptedValues = encrypt(`username=${username}&password=${password}`);
    this.authService.login(encryptedValues).subscribe(
      data => {
        // this.tokenStorage.saveToken(data.accessToken);
        // this.login(data.user);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.router.navigate(['/dashboard']);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  login(user): void {
    this.tokenStorage.saveUser(user);
    this.isLoginFailed = false;
    this.isLoggedIn = true;
    this.currentUser = this.tokenStorage.getUser();
    this.roles = this.tokenStorage.getUser().roles;
    this.reloadPage();
  }

  reloadPage(): void {
    this.router.navigate(['/dashboard']);
  }

  signUpPage(): void {
    this.router.navigate(['/register']);
  }

  forgetPasswordPage(): void {
    this.router.navigate(['/forget-password']);
  }
}
