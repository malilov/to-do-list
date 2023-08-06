FROM openjdk:20-jdk
MAINTAINER baeldung.com
COPY target/to-do-list-1.0.0.jar to-do-list-1.0.0.jar
ENTRYPOINT ["java","-jar","/to-do-list-1.0.0.jar"]