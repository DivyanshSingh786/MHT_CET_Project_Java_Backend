# Stage 1: Build the executable JAR using Maven & JDK 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first and cache Maven dependencies (including Spring Snapshots)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the Spring Boot fat JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR inside a lightweight JRE 21 container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy only the compiled JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the service port (8084 for identity-service)
EXPOSE 8084

# Launch the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]