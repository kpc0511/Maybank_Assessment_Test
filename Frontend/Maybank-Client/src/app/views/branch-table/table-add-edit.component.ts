import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { TokenStorageService } from '../../_services/token-storage.service';
import { UserModel } from '../../data-model/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import {TableService} from '../../_services/table.service';
import {TableModel} from '../../data-model/table.model';
import {AuthService} from '../../_services/auth.service';
import {CompanyService} from '../../_services/company.service';

@Component({
  templateUrl: 'table-add-edit.component.html',
  styleUrls: ['./table-add-edit.component.css']
})
export class TableAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public tableModel: TableModel = new TableModel();
  public editTableId!: number;
  public isAddMode!: boolean;
  public imageid?: bigint;
  public selectedFiles?: FileList;
  private previews: string[] = [];
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private authService: AuthService,
              private tableService: TableService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editTableId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editTableId;
    if (!this.isAddMode) {
      this.tableService.getTableById(this.editTableId).subscribe(
        data => {
          this.tableModel = data;
          if (data.company['id'] !== null) {
            this.tableModel.companyId = data.company['id'];
          }
        },
        err => {
          this.errorMessage = err.error.message;
        }
      );
    }

    this.companyService.getAllCompanies().subscribe(
      data => {
        this.dropdownCompanyList = data;
      }, err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  onSubmit(): void {
    this.tableService.saveTable(this.editTableId, this.tableModel.tableName, this.tableModel.tableType,
      this.tableModel.numberOfSeat, this.user['displayName'], this.tableModel.companyId).subscribe(
      data => {
        this.isSuccessful = true;
        this.isFailed = false;
        this.successMessage = data.message;
        this.removeAlert();
      },
      err => {
        console.log(err);
        this.errorMessage = err.error.message;
        this.isFailed = true;
      }
    );
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isSuccessful = false;
    }, 3000);
  }

  back2List() {
    this.router.navigate(['/tableManage/table-list']);
  }

}
