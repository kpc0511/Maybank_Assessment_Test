export class BranchModel {
  id?: bigint;
  branchName?: string;
  address1?: string;
  address2?: string;
  address3?: string;
  state?: string;
  city?: string;
  postcode?: string;
  createDate?: any;
  createBy?: string;
  updateDate?: any;
  updateBy?: string;
  image: any;
  branchTableList: any[];
  giftsList: any[];
  promotionList: any[];
  companyId?: bigint;
  company: any;
}
