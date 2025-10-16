import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'forget-password.component.html'
})
export class ForgetPasswordComponent implements OnInit {
  form: any = {
    email: null
  };
  isSuccessful = false;
  isResetFailed = false;
  successMessage = '';
  errorMessage = '';

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    const { email} = this.form;

    this.authService.forgetPassword(email).subscribe(
      data => {
        this.isSuccessful = true;
        this.successMessage = data.message;
        this.isResetFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isResetFailed = true;
      }
    );
  }

  loginPage(): void {
    this.router.navigate(['/login']);
  }

  reloadPage(): void {
    window.location.reload();
  }
}
