# **Calculadora de Empréstimos**

Este projeto é uma aplicação full-stack para cálculo de amortização de empréstimos, composta por dois serviços:

1. **Backend (Java Spring Boot):** Uma API REST que contém a lógica de negócios e os algoritmos de cálculo.  
2. **Frontend (React):** Uma interface de usuário moderna para entrada de dados e visualização dos resultados em uma tabela de amortização.

O objetivo deste documento é fornecer instruções claras para que qualquer pessoa consiga executar o projeto em seu ambiente.

## **1\. Execução com Docker (Método Recomendado)**

A utilização do Docker e Docker Compose é o método mais rápido e confiável, pois isola as dependências e garante que a aplicação rode em um ambiente padronizado.

### **Pré-requisitos**

* **Docker:** Instalado e rodando (incluindo o Docker Compose).

### **Passos para Iniciar**

1. Navegue até o Diretório Raiz  
   Certifique-se de estar no diretório que contém o arquivo docker-compose.yml.  
2. Construa e Inicie os Contêineres  
   Execute o comando a seguir. O argumento \--build forçará a construção das imagens Java e Node/Nginx com base nos seus Dockerfiles, garantindo que o código mais recente seja empacotado.  
   docker-compose up \--build \-d

   * O comando \-d (detach) executa os contêineres em segundo plano.  
3. Acesse a Aplicação  
   Após o Docker Compose finalizar o build e a inicialização (o que pode levar alguns minutos na primeira vez), os serviços estarão disponíveis:  
   * **Frontend (Calculadora):** http://localhost:3000  
   * **Backend (API):** http://localhost:8080 (Acesso direto à API para testes).

### **Comandos Úteis do Docker**

| Comando | Descrição |
| :---- | :---- |
| docker-compose up \-d | Inicia os serviços em segundo plano (sem rebuild). |
| docker-compose down | Para e remove os contêineres, redes e volumes criados. |
| docker-compose logs \-f | Exibe os logs de todos os serviços em tempo real. |

## **2\. Execução Local (Sem Docker)**

Este método é ideal para desenvolvedores que precisam modificar o código-fonte (Backend ou Frontend). Requer a instalação manual das dependências de cada ambiente.

### **2.1. Pré-requisitos Locais**

| Serviço | Ferramenta | Versão Mínima |
| :---- | :---- | :---- |
| **Backend** | **JDK (Java Development Kit)** | 21 |
| **Frontend** | **Node.js** | 20.x |
| **Geral** | **Maven** (Opcional, pois o mvnw está incluso) | 3.x |
| **Geral** | **npm** ou **yarn** | Mais recente |

### **2.2. Iniciando o Backend (Java Spring Boot)**

O backend deve ser iniciado primeiro, pois o frontend depende dele.

1. **Navegue até o diretório do backend:**  
   cd backend/calculadora-emprestimos

2. Compile o Projeto  
   Utilize o wrapper Maven (mvnw) para compilar e empacotar a aplicação, pulando os testes:  
   ./mvnw clean package \-DskipTests

3. Execute o JAR  
   Inicie o servidor Spring Boot com o arquivo JAR gerado no diretório target/:  
   java \-jar target/\*.jar

   * O Backend estará rodando em http://localhost:8080.

### **2.3. Iniciando o Frontend (React)**

1. **Navegue até o diretório do frontend:**  
   cd frontend/calculadora-emprestimos-ui

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