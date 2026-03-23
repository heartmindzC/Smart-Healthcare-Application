# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# --- BƯỚC 1: Cài đặt common-exception ---
WORKDIR /app/common-exception
COPY common-exception/pom.xml .
COPY common-exception/src ./src
RUN mvn clean install -DskipTests

WORKDIR /app/EHR-service
COPY EHR-service/pom.xml .
COPY EHR-service/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/EHR-service/target/*.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]