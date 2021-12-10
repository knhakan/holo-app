FROM openjdk:latest
EXPOSE 8080
COPY target/demo-0.0.1-SNAPSHOT.jar demoapp-service.jar
ENTRYPOINT ["java","-jar","/demoapp-service.jar"]