#Dockerfile to builds loggers.
FROM eclipse-temurin:11.0.15_10-jdk as builder
RUN apk --no-cache add curl
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build
#RUN ./gradlew jibDockerBuild

FROM eclipse-temurin:11.0.15_10-jdk
COPY --from=builder /build/libs/devplt-0.0.1-SNAPSHOT.jar /app.jar
#COPY build/libs/*.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8888
ENTRYPOINT exec java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9999 -jar /app.jar
