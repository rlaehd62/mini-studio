FROM openjdk:11

EXPOSE 8084

ADD build\libs\COMMENT_SERVER-0.0.1-DEV.war COMMENT_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "COMMENT_SERVER.war"]