export class CreditModel {
  id?: bigint;
  creditTitle?: string;
  creditName?: string;
  description?: string;
  deductCredit?: number;
  rankingDetailId?: bigint;
  createDate?: any;
  createBy?: string;
  updateDate?: any;
  updateBy?: string;
  companyId?: bigint;
  company: any;
}
