# ============================================
# Stage 1: Build the application with Maven
# ============================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application (downloads deps + compiles + packages)
RUN mvn clean package -DskipTests -B

# ============================================
# Stage 2: Runtime (JRE only — smaller image)
# ============================================
FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="Kushagra Sharma"
LABEL description="SCM2.0 Smart Contact Manager"

WORKDIR /app

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy JAR from builder stage
COPY --from=builder /build/target/scm2.0-0.0.1-SNAPSHOT.jar app.jar

# Set ownership
RUN chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Expose application port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
