FROM ubuntu:16.04

RUN apt-get -y  update

RUN apt-get install -y gradle

RUN apt-get install -y openjdk-8-jdk-headless

ENV WORK /highload
WORKDIR $WORK/

ADD . .
RUN gradle wrapper
RUN ./gradlew build

EXPOSE 80

CMD java -jar build/libs/highload-1.0-SNAPSHOT.jar