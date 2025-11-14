import React from 'react';
import type { LoanInstallment } from '../../types/loan';
import { formatCurrency, formatDate } from '../../utils/dateUtils';

interface Props {
  lines: LoanInstallment[];
}

/**
 * Componente que renderiza a tabela de resultados.
 */
export const LoanTable: React.FC<Props> = ({ lines }) => {
  return (
    <div className="table-container">
      <table>
        <thead>
          <tr>
            <th colSpan={3}>Empréstimo</th>
            <th colSpan={2}>Parcela</th>
            <th colSpan={2}>Principal</th>
            <th colSpan={3}>Juros</th>
          </tr>
          
          <tr>
            <th>Data Competência</th>
            <th>Valor de Empréstimo</th>
            <th>Saldo Devedor</th>
            
            <th>Consolidada</th>
            <th>Total</th>
            <th>Amortização</th>
            
            <th>Saldo</th>
            <th>Provisão</th>

            <th>Acumulado</th>
            <th>Pago</th>
          </tr>
        </thead>
        <tbody>
          {lines.map((line, index) => (
            <tr key={index}>
              <td>{formatDate(line.effectiveDate)}</td>
              <td>{line.loanAmount > 0 ? formatCurrency(line.loanAmount) : '-'}</td>
              <td>{formatCurrency(line.outstandingBalance)}</td>
              <td>{line.consolidated || '-'}</td>
              <td>{line.totalPayment > 0 ? formatCurrency(line.totalPayment) : '-'}</td>
              <td>{line.amortization > 0 ? formatCurrency(line.amortization) : '-'}</td>
              <td>{formatCurrency(line.principalBalance)}</td>
              <td>{formatCurrency(line.provision)}</td>
              <td>{formatCurrency(line.accumulated)}</td>
              <td>{line.paid > 0 ? formatCurrency(line.paid) : '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};