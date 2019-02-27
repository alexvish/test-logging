FROM openjdk:8

COPY ./target/test-logging-1.0-SNAPSHOT.jar /test-logging.jar

CMD  java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /test-logging.jar
