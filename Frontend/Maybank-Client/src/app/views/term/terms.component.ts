import {Component, OnInit} from '@angular/core';
import { first } from 'rxjs/operators';
import {AuthService} from '../../_services/auth.service';
import {TermModel} from '../../data-model/term.model';
import {TermService} from '../../_services/term.service';

@Component({
  templateUrl: 'terms.component.html'
})
export class TermsComponent implements OnInit {
  user = null;
  page = 1;
  title = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  termConditions: TermModel[] = [];
  currentIndex = -1;
  currentTermModel: TermModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private termService: TermService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveTerms();
  }

  retrieveTerms(): void {
    this.termService.getTermListing(this.title, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { termConditions, totalItems } = data;
        this.termConditions = termConditions;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveTerm(termModel: TermModel, index: number): void {
    this.currentTermModel = termModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveTerms();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveTerms();
  }

  searchTitle(): void {
    this.page = 1;
    this.retrieveTerms();
  }

  deleteTerm(id: bigint) {
    const getBranch = this.termConditions.find(x => x.id === id);
    if (!getBranch) { return; }
    this.termService.deleteTerm(getBranch.id).pipe(first()).subscribe(
      data => {
        this.isDeleteSuccessful = true;
        this.successDeleteMessage = data.message;
        this.ngOnInit();
        this.removeAlert();
      },
      err => {
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
