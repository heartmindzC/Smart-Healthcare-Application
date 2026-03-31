# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app/api-gateway-service
COPY api-gateway-service/pom.xml .
COPY api-gateway-service/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/api-gateway-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]


