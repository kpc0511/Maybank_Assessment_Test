import {Component, OnInit} from '@angular/core';
import { TokenStorageService } from '../../_services/token-storage.service';
import { UserModel } from '../../data-model/user.model';
import { first } from 'rxjs/operators';
import {TableService} from '../../_services/table.service';
import {TableModel} from '../../data-model/table.model';

@Component({
  templateUrl: 'tables.component.html'
})
export class TablesComponent implements OnInit {
  user = null;
  tableName = '';
  page = 1;
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  tables: TableModel[] = [];
  currentIndex = -1;
  currentTableModel: TableModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private tableService: TableService, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.user = this.tokenStorageService.getUser();
    this.retrieveTables();
  }

  retrieveTables(): void {
    this.tableService.getTableListing(this.tableName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { tables, totalItems } = data;
        this.tables = tables;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveTable(tableModel: TableModel, index: number): void {
    this.currentTableModel = tableModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveTables();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveTables();
  }

  searchTableName(): void {
    this.page = 1;
    this.retrieveTables();
  }

  deleteTable(id: bigint) {
    const getTable = this.tables.find(x => x.id === id);
    if (!getTable) { return; }
    this.tableService.deleteTable(getTable.id).pipe(first()).subscribe(
      data => {
        this.isDeleteSuccessful = true;
        this.successDeleteMessage = data.message;
        this.ngOnInit();
        this.removeAlert();
      },
      err => {
        console.log(err);
        this.isDeleteFailed = true;
        this.errorDeleteMessage = err;
      }
    );
  }

  removeAlert(): void {
    setTimeout(() => {
      this.isDeleteSuccessful = false;
    }, 3000);
  }
}
