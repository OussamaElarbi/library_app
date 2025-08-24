# Stage 1: Build the app
FROM maven:3.9.11-amazoncorretto-17-debian-trixie as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use Java 17
FROM openjdk:17-jdk-slim
# Set a working directory inside container
WORKDIR /app
# Copy the jar file built from Spring Boot
ARG JAR_FILE=target/*.jar
COPY --from=build /app/target/*.jar application.jar
# Expose app port (default 8080, change if needed)
EXPOSE 8080
# Run the app
ENTRYPOINT ["java", "-jar", "application.jar"]