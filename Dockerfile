# TODO: after docker hub downtime, check if the images are optimal
FROM openjdk:11-jdk as builder
WORKDIR /root
COPY . .
RUN ./gradlew build

FROM openjdk:11-jre-slim
WORKDIR /root
COPY --from=builder /root/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "./app.jar"]