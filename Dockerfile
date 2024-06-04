FROM openjdk:17.0.2-jdk-oracle
LABEL authors="whomai"

ADD bootstrap.sh /home
ADD config.properties /home

ENTRYPOINT ["bash", "/home/bootstrp.sh", "start", "corgi"]