FROM openjdk:17.0.1-jdk-slim

RUN apt-get update && apt-get -y install vim
RUN echo Asia/Seoul > /etc/timezone

WORKDIR /app

ARG JAR_FILE=stock-observer_java-1.0-SNAPSHOT.jar
ARG RESOURCES=resources

COPY ${JAR_FILE} /app/stock-observer.jar
COPY ${RESOURCES} /app/src/main/resources

ENTRYPOINT ["java", "-jar", "stock-observer.jar"]
