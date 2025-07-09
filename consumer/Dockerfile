# Build stage: compila só o módulo consumer usando o POM pai
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia o POM pai e ambos módulos
COPY pom.xml .
COPY producer ./producer
COPY consumer ./consumer

# Compila apenas o módulo consumer
RUN mvn clean package -pl consumer -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR fat gerado
COPY --from=build /app/consumer/target/consumer-1.0-SNAPSHOT.jar app.jar

# Permite mapear o log para fora
VOLUME ["/app/chat.log"]

ENTRYPOINT ["java", "-jar", "app.jar"]
