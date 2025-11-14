export interface LoanRequest {
    startDate: string;
    finalDate: string;
    firstPaymentDate: string;
    loanAmount: number;
    interestRate: number;
}

export interface LoanInstallment {
  effectiveDate: string;
  loanAmount: number;
  outstandingBalance: number;
  consolidated: string | null; 
  totalPayment: number; 
  amortization: number;
  principalBalance: number;
  provision: number; 
  accumulated: number; 
  paid: number;
}