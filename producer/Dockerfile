# 1) Build stage: compila só o módulo producer usando o POM pai
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o POM pai e ambos módulos
COPY pom.xml .
COPY producer ./producer
COPY consumer ./consumer

# Compila apenas o módulo producer
RUN mvn clean package -pl producer -am -DskipTests

# 2) Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR “fat” gerado
COPY --from=build /app/producer/target/producer-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
