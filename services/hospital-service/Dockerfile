# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# --- BƯỚC 1: Cài đặt common-exception ---
WORKDIR /app/common-exception
COPY common-exception/pom.xml .
COPY common-exception/src ./src
RUN mvn clean install -DskipTests

WORKDIR /app/hospital-service
COPY hospital-service/pom.xml .
COPY hospital-service/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/hospital-service/target/*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]