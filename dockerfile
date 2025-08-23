# Use Java 17
FROM openjdk:17-jdk-slim

# Set a working directory inside container
WORKDIR /app

# Copy the jar file built from Spring Boot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

# Expose app port (default 8080, change if needed)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "application.jar"]