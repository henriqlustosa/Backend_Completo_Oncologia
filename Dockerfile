FROM openjdk:8-jre-alpine3.9

WORKDIR /app

COPY target/*.jar /app/livraria-api.jar

EXPOSE 8080

CMD java -XX:+UseContainerSupport -jar livraria-api.jar