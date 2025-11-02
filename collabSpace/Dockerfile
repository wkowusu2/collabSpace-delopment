# ======================
# Stage 1 - Build
# ======================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy only pom.xml first to leverage Docker caching
COPY pom.xml ./

# Download dependencies first (this speeds up builds if source changes)
RUN mvn dependency:go-offline -B

# Copy the rest of the project
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ======================
# Stage 2 - Runtime
# ======================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Use environment variables for Spring configuration
ENV SPRING_DATASOURCE_URL=${DATABASE_URL}
ENV SPRING_DATASOURCE_USERNAME=${DATABASE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${DATABASE_PASSWORD}
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
