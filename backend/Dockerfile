# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copier et builder
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le JAR depuis le stage build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8003

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]