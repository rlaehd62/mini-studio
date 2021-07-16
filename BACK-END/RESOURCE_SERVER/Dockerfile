FROM openjdk:11

EXPOSE 8083

ADD build/libs/RESOURCE_SERVER-0.0.1-DEV.war RESOURCE_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "RESOURCE_SERVER.war"]