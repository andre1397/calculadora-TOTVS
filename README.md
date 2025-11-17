# **Calculadora de Empréstimos**

Este projeto é uma aplicação full-stack para cálculo e geração de uma tabela de amortização de empréstimos, composta por dois serviços:

1. **Backend (Java Spring Boot):** Uma API REST que contém a lógica de negócios e os algoritmos de cálculo.  
2. **Frontend (React):** Uma interface de usuário para entrada de dados e visualização dos resultados em uma tabela de amortização.

## **1\. Execução com Docker**

### **Pré-requisitos**

* **Docker:** Instalado e rodando na máquina.

### **Passos para Iniciar**

1. Construa e Inicie os Contêineres  
   Abra no terminal a pasta raiz da aplicação onde está localizado o arquivo docker-compose e execute o comando:

   docker-compose up \--build \-d

   * O argumento \--build forçará a construção das imagens Java e Node/Nginx com base nos Dockerfiles contidos no Backend e no Frontend.  

   * O comando \-d (detach) é opcional, ele executa os contêineres em segundo plano.  
2. Acesse a Aplicação  
   Após o Docker Compose finalizar o build e a inicialização, os seguintes serviços estarão disponíveis:  
   * **Frontend (Calculadora):** http://localhost:3000  
   * **Backend (API):** http://localhost:8080 (Acesso direto à API para testes).
  
**Observação sobre o Docker Compose:** O contêiner do Frontend aguarda a inicialização do contêiner do Backend (depends\_on: \- backend) e mapeia a porta 80 interna do Nginx para a porta 3000 externa do seu host.

### **Comandos Úteis do Docker**

| Comando | Descrição |
| :---- | :---- |
| docker-compose up \-d | Inicia os serviços em segundo plano (sem rebuild). |
| docker-compose down | Para e remove os contêineres, redes e volumes criados. |
| docker-compose logs \-f | Exibe os logs de todos os serviços em tempo real. |

## **2\. Execução Local (Sem Docker)**

Requer a instalação manual das dependências de cada ambiente.

### **2.1. Pré-requisitos Locais**

| Serviço | Ferramenta | Versão Mínima |
| :---- | :---- | :---- |
| **Backend** | **JDK (Java Development Kit)** | 21 |
| **Frontend** | **Node.js** | 20.x |
| **Geral** | **Maven** (Opcional, pois o mvnw está incluso) | 3.x |
| **Geral** | **npm** | Mais recente |

### **2.2. Iniciando o Backend (Java Spring Boot)**

O Backend deve ser iniciado primeiro, porque o Frontend depende dele para fazer os cálculos.

1. **Navegue até o diretório do Backend:**  
   Abra o diretório calculadora-TOTVS/backend/calculadora-emprestimos no terminal

2. Compile o Projeto  
   Utilize o seguinte comando para o Maven compilar e empacotar a aplicação:
   
   mvnw clean package

   ou se quiser pular a execção dos testes unitários, use:

   mvnw clean package -DskipTests

4. Execute o JAR  
   Inicie o servidor Spring Boot com o arquivo JAR gerado em backend/calculadora-emprestimos/target que terá o nome totvs-calculator-api-0.0.1-SNAPSHOT.jar com o comando:
   
   java \-jar target/totvs-calculator-api-0.0.1-SNAPSHOT.jar

   * O Backend rodará em http://localhost:8080 (Acesso direto sem Frontend à API para testes).

### **2.3. Iniciando o Frontend (React)**

1. **Navegue até o diretório do Frontend**  
   Abra o diretório calculadora-TOTVS/frontend/calculadora-emprestimos-ui no terminal

2. **Instale as Dependências**
   Para instalar as depêndencias do Frontend, no terminal execute o comando:
   
   npm install

4. **Inicie o Servidor de Desenvolvimento**
   Para iniciar a execução do Frontend, no terminal execute o comando:
   
   npm run dev 

   * O Frontend estará acessível em http://localhost:5173.

# Testes Unitários (CalculatorServiceTest)
Os testes unitários garantem a precisão do motor de cálculo de amortização e a correta aplicação das regras de negócio, como o ajuste de datas e a inclusão das linhas de provisão.

1. Execução dos Testes
Para rodar todos os testes unitários do Backend do projeto, execute na no terminal da raiz do Backend (calculadora-TOVS/backend/calculadora-emprestimos):

   mvn test

2. Detalhes da Tabela de Amortização (SAC)
   O CalculatorServiceTest foi contruído para validar a estrutura detalhada do LoanInstallmentDto, que exige a inclusão de linhas de provisão de juros.
   
   * Comportamento Esperado: Para um empréstimo SAC, a tabela de amortização deve incluir todos os eventos de cálculo de juros, mesmo que não sejam um pagamento.
   
   * O principal teste (calculate_SacLoanWithProvisionLines) verifica se a lista de parcelas gerada sempre contém:
   
      - 1 Linha Inicial (Saldo Devedor na Data de Início).
   
      - Linhas de Provisão: Uma linha no último dia de cada mês para registrar a provisão de juros acumulados (Saldo Devedor Capital + Juros Acumulados).
   
      - Linhas de Pagamento: Uma linha na data de pagamento ajustada (próximo dia útil) que zera o saldo de juros acumulados (accumulated) e registra a amortização (amortization).
   
   Exemplo:
   Para um empréstimo de 3 parcelas, o sistema deve gerar 7 linhas de evento (1 Início + 3 Provisões + 3 Pagamentos) para garantir a conformidade com os requisitos contábeis de provisão mensal.

3. Validação de Regras
   Os testes também verificam:
   
   * Ajuste de Datas: Datas de pagamento que caem em finais de semana ou feriados são corretamente ajustadas para o próximo dia útil.
   
   * Arredondamento Financeiro: Os saldos e amortizações são verificados em BigDecimal para garantir que o arredondamento de centavos esteja correto ao longo das parcelas.
