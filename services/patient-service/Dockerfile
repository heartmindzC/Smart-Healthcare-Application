# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# --- BƯỚC 1: Cài đặt common-exception ---
WORKDIR /app/common-exception
COPY common-exception/pom.xml .
COPY common-exception/src ./src
RUN mvn clean install -DskipTests

# --- BƯỚC 2: Build user-service ---
WORKDIR /app/patient-service
# ĐÃ SỬA: Thêm 'user-service/' vào trước để Docker tìm đúng file
COPY patient-service/pom.xml .
COPY patient-service/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/patient-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]