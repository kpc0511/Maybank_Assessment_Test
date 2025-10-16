import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { first } from 'rxjs/operators';
import {BranchModel} from '../../data-model/branch.model';
import {BranchService} from '../../_services/branch.service';
import {CompanyModel} from '../../data-model/company.model';
import {CompanyService} from '../../_services/company.service';
import {CreditService} from '../../_services/credit.service';
import {AuthService} from '../../_services/auth.service';

@Component({
  templateUrl: 'companies.component.html'
})
export class CompaniesComponent implements OnInit {
  user = null;
  page = 1;
  customerId = '';
  description = '';
  accountNumber = '';
  provider = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 10;
  pageSizes = [10, 20, 30];
  companies: CompanyModel[] = [];
  currentIndex = -1;
  currentCompanyModel: CompanyModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private companyService: CompanyService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveCompanies();
  }

  retrieveCompanies(): void {
    const accountNumbers = this.accountNumber.trim() ? this.accountNumber.split(',').map(account => account.trim()) : null;
    this.companyService.getTransactionListing(this.customerId, this.description, accountNumbers,
      this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        console.log(data);
        const { content, totalElements } = data.data;
        this.companies = content;
        this.count = totalElements;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveCompany(companyModel: CompanyModel, index: number): void {
    this.currentCompanyModel = companyModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveCompanies();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveCompanies();
  }

  searchTransaction(): void {
    this.page = 1;
    this.retrieveCompanies();
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isDeleteSuccessful = false;
    }, 3000);
  }

  getImage(imageData: any) {
    if (imageData === null) {
      return 'assets/img/users/default-user.jpg';
    } else {
      const imageBase64 = 'data:image/jpeg;base64,' + imageData.data;
      return imageBase64;
    }
  }
}
