FROM adoptopenjdk:11-jre-hotspot
RUN mkdir -p /appl/bin/
RUN mkdir -p /appl/logs/
ADD target/desafio-communication-scheduling-0.0.1-SNAPSHOT.jar /appl/bin/app.jar
ENTRYPOINT ["java","-jar","/appl/bin/app.jar"]
EXPOSE 8080
