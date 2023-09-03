# Stage 1: Build the application
FROM ubuntu:20.04 AS build

WORKDIR /home/gradle/project

RUN apt-get update;
RUN apt-get install -y openjdk-17-jdk;
RUN apt-get install -y unzip zip;
RUN apt-get clean;

COPY . .

# RUN mkdir -p /root/.gradle
# RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

RUN cd auth-service
RUN ./gradlew clean build

# Stage 2: Run the application
FROM ubuntu:20.04

WORKDIR /root/workspace

RUN apt-get update;
RUN apt-get install -y openjdk-17-jre;
RUN apt-get clean;

COPY --from=build /home/gradle/project/auth-service/build/libs/auth-server-1.0.jar .

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "auth-server-1.0.jar"]

EXPOSE 8080
