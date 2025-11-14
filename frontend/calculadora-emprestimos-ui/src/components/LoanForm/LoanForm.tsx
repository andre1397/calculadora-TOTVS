import React, { useState, useMemo } from 'react';
import type { LoanRequest } from '../../types/loan';

interface Props {
  onSubmit: (data: LoanRequest) => void;
  loading: boolean;
}

/**
 * Componente que renderiza o formulário de 5 campos.
 */
export const LoanForm: React.FC<Props> = ({ onSubmit, loading }) => {
  const [formData, setFormData] = useState({
    startDate: '',
    finalDate: '',
    firstPaymentDate: '',
    loanAmount: '',
    interestRate: '',
  });

  const [validationError, setValidationError] = useState<string | null>(null);

  /**
   * Atualiza o estado do formulário a cada mudança em um input.
   */
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  /**
   * Valida se o botão "Calcular" deve estar habilitado.
   */
  const isFormValid = useMemo(() => {
    const { startDate, finalDate, firstPaymentDate, loanAmount, interestRate } = formData;
    if (!startDate || !finalDate || !firstPaymentDate || !loanAmount || !interestRate) {
      return false;
    }
    if (parseFloat(loanAmount) <= 0 || parseFloat(interestRate) <= 0) {
      return false;
    }
    return true;
  }, [formData]);

  /**
   * Chamado quando o formulário é enviado.
   * Executa a validação de regras de negócio do frontend.
   */
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setValidationError(null);

    const { startDate, finalDate, firstPaymentDate } = formData;
    if (finalDate <= startDate) {
      setValidationError('Erro ao calcular: A data final deve ser maior que a data inicial.');
      return;
    }
    if (firstPaymentDate <= startDate || firstPaymentDate >= finalDate) {
      setValidationError('Erro ao calcular: O primeiro pagamento deve ser entre a data inicial e a final.');
      return;
    }
    
    onSubmit({
      startDate: formData.startDate,
      finalDate: formData.finalDate,
      firstPaymentDate: formData.firstPaymentDate,
      loanAmount: parseFloat(formData.loanAmount),
      interestRate: parseFloat(formData.interestRate) / 100,
    });
  };

  return (
    <form onSubmit={handleSubmit} className="loan-form">
      <div className="form-grid">
        <div className="form-group">
          <label htmlFor="startDate">Data Inicial</label>
          <input
            type="date"
            id="startDate"
            name="startDate"
            value={formData.startDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="finalDate">Data Final</label>
          <input
            type="date"
            id="finalDate"
            name="finalDate"
            value={formData.finalDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="firstPaymentDate">Primeiro Pagamento</label>
          <input
            type="date"
            id="firstPaymentDate"
            name="firstPaymentDate"
            value={formData.firstPaymentDate}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="loanAmount">Valor do Empréstimo</label>
          <input
            type="number"
            id="loanAmount"
            name="loanAmount"
            value={formData.loanAmount}
            onChange={handleChange}
            step="0.01"
            min="0.01"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="interestRate">Taxa de Juros (em %)</label>
          <input
            type="number"
            id="interestRate"
            name="interestRate"
            value={formData.interestRate}
            onChange={handleChange}
            step="0.01"
            min="0.01"
            required
          />
        </div>
      </div>
      {validationError && <div className="error-message">{validationError}</div>}
      <button type="submit" disabled={!isFormValid || loading}>
        {loading ? 'Calculando...' : 'Calcular'}
      </button>
    </form>
  );
};