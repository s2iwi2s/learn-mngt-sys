ARG BASE_IMAGE

FROM $BASE_IMAGE

ARG JAVA_OPTS
ARG JHIPSTER_SLEEP
ARG SPRING_PROFILES_ACTIVE
ARG SPRING_DATASOURCE_URL
ARG SPRING_LIQUIBASE_URL

ENV JAVA_OPTS $JAVA_OPTS
ENV JHIPSTER_SLEEP $JHIPSTER_SLEEP
ENV SPRING_PROFILES_ACTIVE $SPRING_PROFILES_ACTIVE
ENV SPRING_DATASOURCE_URL $SPRING_DATASOURCE_URL
ENV SPRING_LIQUIBASE_URL $SPRING_LIQUIBASE_URL

VOLUME /tmp

COPY ./target/learn-mngt-sys-0.0.1-SNAPSHOT.jar /tmp/app.jar
#COPY ./src/main/docker/jib/env_setup.sh ./
COPY ./src/main/docker/jib/entrypoint.sh ./
RUN pwd
#RUN  chmod  +x  ./env_setup.sh
RUN chmod  +x  ./entrypoint.sh

EXPOSE 8181

#RUN ./env_setup.sh
ENTRYPOINT ["/bin/bash", "-c", "./entrypoint.sh"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom","-jar","/tmp/app.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/urandom", "-Dspring.profiles.active=prod,api-docs,no-liquibase", "-DSPRING_DATASOURCE_URL=jdbc:postgresql://192.168.110.136:5432/LearnMngtSys", "-DSPRING_LIQUIBASE_URL==jdbc:postgresql://192.168.110.136:5432/LearnMngtSys", "-Dusername: LearnMngtSys", "-jar","/tmp/app.jar"]
