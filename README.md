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

   * O argumento \--build forçará a construção das imagens Java e Node/Nginx com base nos Dockerfiles contidos no backend e no frontend.  

   * O comando \-d (detach) executa os contêineres em segundo plano.  
2. Acesse a Aplicação  
   Após o Docker Compose finalizar o build e a inicialização, os seguintes serviços estarão disponíveis:  
   * **Frontend (Calculadora):** http://localhost:3000  
   * **Backend (API):** http://localhost:8080 (Acesso direto à API para testes).

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
| **Geral** | **npm** ou **yarn** | Mais recente |

### **2.2. Iniciando o Backend (Java Spring Boot)**

O backend deve ser iniciado primeiro, porque o frontend depende dele para fazer os cálculos.

1. **Navegue até o diretório do backend:**  
   Abra o diretório calculadora-TOTVS/backend/calculadora-emprestimos no terminal

2. Compile o Projeto  
   Utilize o seguinte comando para o Maven compilar e empacotar a aplicação:  
   ./mvnw clean package

3. Execute o JAR  
   Inicie o servidor Spring Boot com o arquivo JAR gerado em backend/calculadora-emprestimos/target que terá o nome totvs-calculator-api-0.0.1-SNAPSHOT.jar com o comando:  
   java \-jar target/totvs-calculator-api-0.0.1-SNAPSHOT.jar

   * O Backend rodará em http://localhost:8080.

### **2.3. Iniciando o Frontend (React)**

1. **Navegue até o diretório do frontend:**  
   Abra o diretório calculadora-TOTVS/frontend/calculadora-emprestimos-ui no terminal

2. **Instale as Dependências**  
   npm install

3. **Inicie o Servidor de Desenvolvimento**  
   npm run dev  
   \# ou, dependendo da sua configuração:  
   npm start

   * O Frontend estará acessível em http://localhost:3000.

## **3\. Estrutura e Configurações**

| Componente | Diretório | Tecnologia | Porta | Perfil Docker |
| :---- | :---- | :---- | :---- | :---- |
| **Backend** | ./backend/calculadora-emprestimos | Java 21, Spring Boot | 8080 | prod (via docker-compose) |
| **Frontend** | ./frontend/calculadora-emprestimos-ui | Node 20, React, Nginx | 3000 | N/A |

**Observação sobre o Docker Compose:** O contêiner do Frontend aguarda a inicialização do contêiner do Backend (depends\_on: \- backend) e mapeia a porta 80 interna do Nginx para a porta 3000 externa do seu host.
