import {Component, OnInit} from '@angular/core';
import { first } from 'rxjs/operators';
import {AuthService} from '../../_services/auth.service';
import {TermModel} from '../../data-model/term.model';
import {TermService} from '../../_services/term.service';
import {CreditModel} from '../../data-model/credit.model';
import {CreditService} from '../../_services/credit.service';

@Component({
  templateUrl: 'credits.component.html'
})
export class CreditsComponent implements OnInit {
  user = null;
  page = 1;
  title = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  songCredits: CreditModel[] = [];
  currentIndex = -1;
  currentCreditModel: CreditModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private creditService: CreditService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveCredits();
  }

  retrieveCredits(): void {
    this.creditService.getCreditListing(this.title, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { songCredits, totalItems } = data;
        this.songCredits = songCredits;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveCredit(creditModel: CreditModel, index: number): void {
    this.currentCreditModel = creditModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveCredits();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveCredits();
  }

  searchTitle(): void {
    this.page = 1;
    this.retrieveCredits();
  }

  deleteCredit(id: bigint) {
    const getCredit = this.songCredits.find(x => x.id === id);
    if (!getCredit) { return; }
    this.creditService.deleteSongCredit(getCredit.id).pipe(first()).subscribe(
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
