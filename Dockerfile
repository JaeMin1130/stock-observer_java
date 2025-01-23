FROM openjdk:17.0.1-jdk-slim

RUN apt-get update && apt-get -y install vim
RUN echo Asia/Seoul > /etc/timezone

WORKDIR /app

ARG JAR_FILE=stock-observer_java-1.0-SNAPSHOT.jar
ARG RESOURCES=resources
ARG VIMRC=.vimrc

COPY ${JAR_FILE} /app/stock-observer.jar
COPY ${RESOURCES} /app/src/main/resources
COPY ${VIMRC} /root/.vimrc

ENTRYPOINT ["java", "-jar", "stock-observer.jar"]
