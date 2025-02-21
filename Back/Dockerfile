# 1. 빌드 환경 (Gradle 사용)
FROM openjdk:17-jdk-slim AS builder 
WORKDIR /app
COPY . .

# Gradle 실행을 위해 필요한 패키지 설치
RUN apt-get update && apt-get install -y findutils

RUN chmod +x ./gradlew
RUN ./gradlew build --no-daemon

# 2. 실행 환경
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
 
