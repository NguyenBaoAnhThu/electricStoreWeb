FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copy Gradle Wrapper
COPY gradlew .
COPY gradle gradle

RUN chmod +x ./gradlew

RUN ./gradlew --version --no-daemon || echo "Gradle download may have failed, continuing..."

# Copy toàn bộ source
COPY . .

# Cấp quyền lại cho gradlew sau khi copy source
RUN chmod +x ./gradlew

# Build
RUN ./gradlew build -x test --no-daemon --console=plain

# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/electricStore-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
