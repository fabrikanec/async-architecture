FROM openjdk:11

ENV JAVA_MEM="-Xmx128M"
ENV JAVA_EXT=""

ENV TZ=Europe/Moscow

COPY dependencies/ app
COPY spring-boot-loader/ app
COPY snapshot-dependencies/ app
COPY application/ app

WORKDIR app

CMD java $JAVA_MEM $JAVA_EXT org.springframework.boot.loader.JarLauncher
EXPOSE 8080
