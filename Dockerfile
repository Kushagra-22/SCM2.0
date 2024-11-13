# Use a base image with Java
FROM openjdk:17-jdk-alpine

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod

# Add a volume to keep log files generated in the container
VOLUME /tmp

# Copy the jar file to the container
COPY target/scm2.0-0.0.1-snapshot.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
