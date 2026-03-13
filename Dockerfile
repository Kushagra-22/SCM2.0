FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/scm2.0-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (default 8080 for Spring Boot)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
