# syntax=docker/dockerfile:1

# ---- Build stage: compile the Spring Boot fat jar with the Maven wrapper ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# The Maven wrapper uses distributionType=only-script, so it downloads Maven
# itself at build time via curl (not bundled as a jar in the repo).
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# ---- Runtime stage: run the jar on a slim JRE ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/sidebet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Render injects PORT and the DB connection details (see render.yaml). We bind to
# $PORT and build a jdbc: URL from the discrete host/port/database fields so the
# datasource URL is in the jdbc:postgresql://... form Spring requires (Render's
# DATABASE_URL is postgresql://... which Spring cannot consume directly).
CMD ["sh", "-c", "java -Dspring.profiles.active=prod -Dserver.port=${PORT:-8080} -Dspring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} -jar app.jar"]
