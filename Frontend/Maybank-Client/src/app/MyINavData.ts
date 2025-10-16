import {INavData} from '@coreui/angular';

export interface MyINavData extends INavData {
  role?: string;
  permissions?: string[];
  displayOrder: number;
}
