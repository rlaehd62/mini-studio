FROM openjdk:11

EXPOSE 8081

ADD build\libs\AUTH_SERVER-0.0.1-DEV.war AUTH_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "AUTH_SERVER.war"]