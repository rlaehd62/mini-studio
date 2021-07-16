FROM openjdk:11

EXPOSE 8888

ADD build\libs\API-GATEWAY-0.0.1-SNAPSHOT.war API-GATEWAY.war

ENTRYPOINT ["java", "-server",  "-jar", "API-GATEWAY.war"]