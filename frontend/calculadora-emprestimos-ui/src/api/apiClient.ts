import axios from 'axios';
import type { LoanRequest, LoanInstallment } from '../types/loan';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 15000,
});

/**
 * Função que chama a API de backend para calcular o empréstimo.
 * @param requestData Os dados do formulário.
 * @returns Uma promessa com a tabela de amortização.
 */
export const calculateLoan = async (
  requestData: LoanRequest
): Promise<LoanInstallment[]> => {
  try {
    const response = await api.post('/calculate', requestData);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      throw new Error(error.response.data.message || 'Erro ao calcular');
    }
    throw new Error('Não foi possível conectar ao servidor');
  }
};