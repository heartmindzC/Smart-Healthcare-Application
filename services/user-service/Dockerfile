# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
#COPY services/user-service/pom.xml .
#COPY services/user-service/src ./src

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

