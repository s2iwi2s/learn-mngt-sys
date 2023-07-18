FROM openjdk:11
VOLUME /tmp
ADD target/learn-mngt-sys-0.0.1-SNAPSHOT.jar /tmp/app.jar
# ADD ./src/main/docker/jib/entrypoint.sh .
EXPOSE 8181
#ENTRYPOINT ["/bin/bash", "-c", "/entrypoint.sh"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","/tmp/app.jar"]
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom", "-Dspring.profiles.active=prod,api-docs","-jar","/tmp/app.jar"]
