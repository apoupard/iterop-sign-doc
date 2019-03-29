FROM gradle:jdk11-slim as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build test

FROM openjdk:11-jre-slim

COPY --from=builder /home/gradle/src/sign-doc-rest/build/libs/sign-doc-rest-*.jar sign-doc-rest.jar

EXPOSE 8888

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /sign-doc-rest.jar" ]
