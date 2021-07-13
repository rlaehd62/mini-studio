FROM openjdk:11

EXPOSE 8761

ADD build\libs\EUREKA_SERVER-0.0.1-DEV.war EUREKA_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "EUREKA_SERVER.war"]