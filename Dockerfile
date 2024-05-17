FROM openjdk:17-jdk-alpine
LABEL maintainer="m.semenov"
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon -x test
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/build/libs/calendar-1.0.0.jar"]