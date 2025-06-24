# === STAGE 1: Build Stage ===
FROM openjdk:17.0.2-jdk-slim AS builder

LABEL maintainer="Omkar <your-email@example.com>"
LABEL version="1.0.0"
LABEL description="Builds the Spring Boot JAR using Maven"

WORKDIR /app

# Copy Maven wrapper + configs first to cache dependencies
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies without building the app yet
RUN ./mvnw dependency:go-offline -B

# Now copy source code and build
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# === STAGE 2: Production Image (Slim & Secure) ===
FROM gcr.io/distroless/java17-debian11

ARG UID=1001
ARG GID=1001

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder --chown=${UID}:${GID} /app/target/*.jar ./app.jar

# Run as non-root user
USER ${UID}:${GID}

# Expose app port
EXPOSE 8080

# JVM optimizations
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:+ExitOnOutOfMemoryError"

# Optional: Health check if you use Spring Actuator
# COPY --from=builder /usr/bin/curl /usr/bin/curl
# HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
#   CMD ["/usr/bin/curl", "-f", "http://localhost:8080/actuator/health"] || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

