import { TestBed } from '@angular/core/testing';
import {AppSidebarService} from './app-side-bar.service';

describe('AppSidebarService', () => {
  let service: AppSidebarService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppSidebarService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
