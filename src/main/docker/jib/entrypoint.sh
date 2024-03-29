#!/bin/sh

echo "JAVA_OPTS=>${JAVA_OPTS}"
echo "JHIPSTER_SLEEP=>${JHIPSTER_SLEEP}"
echo "SPRING_DATASOURCE_URL=>${SPRING_DATASOURCE_URL}"
echo "SPRING_LIQUIBASE_URL=>${SPRING_LIQUIBASE_URL}"

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep ${JHIPSTER_SLEEP}
# exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.s2i.lms.LearnMngtSysApp"  "$@"
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -jar /tmp/app.jar "$@"
