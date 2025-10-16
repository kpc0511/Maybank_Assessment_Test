import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'register.component.html'
})
export class RegisterComponent implements OnInit {
  form: any = {
    diplayName: null,
    email: null,
    password: null,
    repeatPassword: null,
    mobileNumber: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  successMessage = '';
  userRoles = ['user'];

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { displayName, email, password,
      mobileNumber} = this.form;

    this.authService.register(displayName, email, password, password, mobileNumber, this.userRoles).subscribe(
      data => {
        this.isSuccessful = true;
        this.isSignUpFailed = false;

        setTimeout(() => {
          this.reloadPage();
        }, 1000);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }

  reloadPage(): void {
    this.router.navigate(['/login']);
  }

  loginPage(): void {
    this.router.navigate(['/login']);
  }
}
