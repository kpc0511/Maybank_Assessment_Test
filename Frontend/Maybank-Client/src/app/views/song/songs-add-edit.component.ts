import { Component, Inject, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import {AuthService} from '../../_services/auth.service';
import {CreditModel} from '../../data-model/credit.model';
import {CreditService} from '../../_services/credit.service';
import {RankService} from '../../_services/rank.service';
import {CompanyService} from '../../_services/company.service';
import {SongModel} from '../../data-model/song.model';
import {SongService} from '../../_services/song.service';

@Component({
  templateUrl: 'songs-add-edit.component.html',
  styleUrls: ['./songs-add-edit.component.css']
})
export class SongsAddEditComponent implements OnInit {
  public user = null;
  public isSuccessful = false;
  public isFailed = false;
  public errorMessage = '';
  public successMessage = '';
  public songModel: SongModel = new SongModel();
  public editSongId!: number;
  public isAddMode!: boolean;
  public imageId?: bigint;
  dropdownCompanyList: Array<any> = [];

  constructor(@Inject(DOCUMENT) private _document: any,
              private songService: SongService,
              private authService: AuthService,
              private companyService: CompanyService,
              private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.editSongId = this.activatedRoute.snapshot.params['id'];
    this.user = this.authService.userValue;
    this.isAddMode = !this.editSongId;
    if (!this.isAddMode) {
      this.songService.getSongById(this.editSongId).subscribe(
        data => {
          this.songModel = data;
          if (data.company['id'] !== null) {
            this.songModel.companyId = data.company['id'];
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
      this.songService.saveSong(this.editSongId, this.songModel.songName, this.songModel.songTitle, this.songModel.songDuration,
        this.user['displayName'], this.songModel.singerId, this.songModel.companyId).subscribe(
        data => {
          this.isSuccessful = true;
          this.isFailed = false;
          this.successMessage = data.message;
          this.removeAlert();
        },
        err => {
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
    this.router.navigate(['/song/song-listing']);
  }
}
