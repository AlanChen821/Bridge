# Use an official Java runtime as a base image
FROM openjdk:17-jdk-alpine
# jre = java runtime environment Java SE = standard edition
# Set the working directory in the container
WORKDIR /app
# Copy the built jar from the Maven build into the container
COPY target/Bridge-0.0.1-SNAPSHOT.jar app.jar
# Expose the port your Spring Boot app runs on
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]