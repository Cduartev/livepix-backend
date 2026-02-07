# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the pom.xml first to verify dependencies (better layer caching)
COPY pom.xml .
# Go offline to cache dependencies
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Render sets PORT env var contextually, but 8080 is default fallback)
ENV PORT=8080
EXPOSE 8080

# Configure JVM options for container support
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
