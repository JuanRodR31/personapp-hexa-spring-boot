# Runtime stage for REST API
FROM eclipse-temurin:17-jre-alpine AS rest
WORKDIR /app
COPY rest-input-adapter/target/rest-input-adapter-*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]

# Runtime stage for CLI
FROM eclipse-temurin:17-jre-alpine AS cli
WORKDIR /app
COPY cli-input-adapter/target/cli-input-adapter-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
