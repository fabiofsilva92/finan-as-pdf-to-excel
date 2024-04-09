# Use a official OpenJDK image as the base image
FROM openjdk:17-jdk-slim as build

# Set the working directory in the container
WORKDIR /app

# Copy the source code into the container
COPY . .

# For Maven:
RUN ./mvnw clean package

# Use OpenJDK 17 JRE for running the application
FROM openjdk:17-jdk-slim

# Copy the built application from the previous stage
COPY --from=build /app/target/*.jar /app/application.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app/application.jar"]