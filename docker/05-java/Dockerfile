FROM openjdk:8-jre-alpine

COPY ./target/docker-webapp-0.0.1-SNAPSHOT.jar /webapp.jar

CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/webapp.jar"]