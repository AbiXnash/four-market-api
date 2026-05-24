FROM gradle:jdk21 AS builder
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle ./gradle
COPY src ./src
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar --no-daemon --no-build-cache

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
