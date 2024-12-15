# 빌드를 위한 베이스 이미지 지정 및 builder라는 이름으로 스테이지 생성
FROM bellsoft/liberica-openjdk-alpine:21 AS builder

# 컨테이너 내부의 작업 디렉토리를 /project로 설정
WORKDIR /project

# Gradle 관련 파일들을 컨테이너로 복사
COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY app app
COPY support support

# Gradle을 사용하여 app 모듈의 jar 파일 생성
RUN ./gradlew clean :app:bootJar --no-daemon

# 실행 환경을 위한 새로운 스테이지 시작
FROM bellsoft/liberica-runtime-container:jre-21-slim-musl

# 실행 환경의 작업 디렉토리 설정
WORKDIR /app

# builder 스테이지에서 생성된 jar 파일을 복사
COPY --from=builder /project/app/build/libs/*.jar app.jar

# 컨테이너 실행 시 java -jar app.jar 명령 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
