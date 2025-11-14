# ====================
# PRIMEIRA FASE (BUILD)
# ====================
# Usa uma imagem base Java 21 para compilar o projeto
FROM eclipse-temurin:21-jdk-jammy as build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração (para otimizar o cache)
COPY pom.xml .

# Copia os arquivos de configuração (para otimizar o cache)
COPY mvnw . 
COPY .mvn .mvn 

# Copia o código-fonte
COPY src ./src

# Compila o projeto Spring Boot
RUN ./mvnw clean package -DskipTests

# ====================
# SEGUNDA FASE (RUNTIME)
# ====================
# Imagem menor, apenas com o JRE 21, para produção
FROM eclipse-temurin:21-jre-jammy

# Argumento para o nome do arquivo JAR gerado
ARG JAR_FILE=target/*.jar

# Copia o JAR compilado da fase 'build' para a imagem de runtime
COPY --from=build /app/${JAR_FILE} app.jar

# Define o comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]