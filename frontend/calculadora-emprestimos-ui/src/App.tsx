import { LoanForm } from './components/LoanForm/LoanForm';
import { LoanTable } from './components/LoanTable/LoanTable';
import { useLoan } from './hooks/useLoan';
import Icone from './assets/money.png';
import './App.css';

function App() {
  const { installments, loading, error, fetchLoan } = useLoan();

  return (
    <div className="app-container">
      <header>
        <img src={Icone} alt="Money icon" className="header-icon" />
        <h1>Calculadora de Empréstimos</h1>
      </header>
      <main>
        <LoanForm onSubmit={fetchLoan} loading={loading} /> 
        
        {error && <div className="error-message">{error}</div>}
        {loading && <div className="loading-message">Calculando...</div>}
        
        {installments.length > 0 && <LoanTable lines={installments} />}
      </main>
    </div>
  );
}

export default App;