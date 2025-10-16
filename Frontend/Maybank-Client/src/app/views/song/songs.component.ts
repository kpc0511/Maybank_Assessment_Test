import {Component, OnInit} from '@angular/core';
import { first } from 'rxjs/operators';
import {AuthService} from '../../_services/auth.service';
import {SongService} from '../../_services/song.service';
import {SongModel} from '../../data-model/song.model';

@Component({
  templateUrl: 'songs.component.html'
})
export class SongsComponent implements OnInit {
  user = null;
  page = 1;
  songName = '';
  sort = ['id,asc'];
  count = 0;
  pageSize = 3;
  pageSizes = [3, 6, 9];
  songs: SongModel[] = [];
  currentIndex = -1;
  currentSongModel: SongModel;
  isDeleteSuccessful = false;
  isDeleteFailed = false;
  errorDeleteMessage = '';
  successDeleteMessage = '';

  constructor(private songService: SongService, private authService: AuthService) { }

  ngOnInit(): void {
    this.user = this.authService.userValue;
    this.retrieveSongs();
  }

  retrieveSongs(): void {
    this.songService.getSongListing(this.songName, this.page, this.pageSize, this.sort).pipe(first()).subscribe(
      data => {
        const { songs, totalItems } = data;
        this.songs = songs;
        this.count = totalItems;
      },
      err => {
        console.log(err);
      }
    );
  }

  setActiveSong(songModel: SongModel, index: number): void {
    this.currentSongModel = songModel;
    this.currentIndex = index;
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.retrieveSongs();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.retrieveSongs();
  }

  searchSongName(): void {
    this.page = 1;
    this.retrieveSongs();
  }

  deleteSong(id: bigint) {
    const getSong = this.songs.find(x => x.id === id);
    if (!getSong) { return; }
    this.songService.deleteSong(getSong.id).pipe(first()).subscribe(
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
