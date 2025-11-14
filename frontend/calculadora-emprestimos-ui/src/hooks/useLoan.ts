import { useState } from 'react';
import { calculateLoan } from '../api/apiClient';
import type { LoanInstallment, LoanRequest } from '../types/loan';

export const useLoan = () => {
  const [installments, setInstallments] = useState<LoanInstallment[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchLoan = async (request: LoanRequest) => {
    setLoading(true);
    setError(null);
    try {
      const data = await calculateLoan(request);
      setInstallments(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Erro ao calcular');
    } finally {
      setLoading(false);
    }
  };

  return { installments, loading, error, fetchLoan, setInstallments };
};
