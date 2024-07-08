FROM gradle:7.6-jdk17

WORKDIR /

COPY / .

RUN gradle build

CMD java -jar ./build/libs/voting-0.0.1-SNAPSHOT.jar