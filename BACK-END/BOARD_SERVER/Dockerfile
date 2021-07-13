FROM openjdk:11

EXPOSE 8082

ADD build\libs\BOARD_SERVER-0.0.1-DEV.war BOARD_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "BOARD_SERVER.war"]