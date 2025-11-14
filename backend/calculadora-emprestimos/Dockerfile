FROM eclipse-temurin:21-jdk-jammy as build

WORKDIR /app

COPY pom.xml .

COPY mvnw . 
COPY .mvn .mvn 

COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

ARG JAR_FILE=target/*.jar

COPY --from=build /app/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]