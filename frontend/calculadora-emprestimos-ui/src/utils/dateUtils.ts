import { format, parseISO } from 'date-fns';
import { ptBR } from 'date-fns/locale';

/**
 * Formata um valor numérico para a moeda brasileira (R$).
 * @param value O valor a ser formatado.
 */
export const formatCurrency = (value: number): string => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
};

/**
 * Formata uma string de data (ex: "2024-01-01") para o padrão brasileiro (dd/MM/yyyy).
 * @param dateString A data vinda da API do backend.
 */
export const formatDate = (dateString: string): string => {
  try {
    const date = parseISO(dateString);
    return format(date, 'dd/MM/yyyy', { locale: ptBR });
  } catch (error) {
    return dateString;
  }
};