# Stage 1: Build stage using JDK 21 Alpine
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker layer caching for dependencies
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies (offline mode)
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the application executable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Minimal Runtime Stage using JRE 21 Alpine (~180MB image)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root system user for security best practices
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy compiled JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
