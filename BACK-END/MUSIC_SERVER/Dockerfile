FROM openjdk:11

EXPOSE 8085

ADD build\libs\MUSIC_SERVER-0.0.1-DEV.war MUSIC_SERVER.war

ENTRYPOINT ["java", "-server", "-jar", "MUSIC_SERVER.war"]