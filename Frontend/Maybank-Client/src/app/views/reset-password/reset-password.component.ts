import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../_services/auth.service';
import { Router, ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {
  form: any = {
    password: null
  };
  isSuccessful = false;
  isResetFailed = false;
  successMessage = '';
  errorMessage = '';
  token = '';

  constructor(private activatedRoute: ActivatedRoute, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      this.token = params['token'];
    });
  }

  onSubmit(): void {
    const { password } = this.form;
    this.authService.resetPassword(this.token, password).subscribe(
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
