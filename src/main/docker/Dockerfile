FROM adoptopenjdk:11-jre-hotspot
VOLUME /tmp
EXPOSE 8082
ARG JAR_FILE=livraria-0.0.1-SNAPSHOT.jar 
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]