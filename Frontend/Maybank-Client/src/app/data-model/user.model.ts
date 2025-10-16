export class UserModel {
  id?: bigint;
  displayName?: string;
  password?: string;
  repeatPassword?: string;
  currentJob?: string;
  remark?: string;
  personalDescription?: string;
  email?: string;
  phone?: string;
  gender?: any;
  dob?: string;
  enabled?: boolean;
  createDate?: any;
  createBy?: string;
  updateDate?: any;
  updateBy?: string;
  lastLoginDate?: any;
  role: any;
  image: any;
  token?: string;
  provider?: string;
  companyId?: bigint;
  company: any;
  credit: number;
  description: string;
}
