#
# Build stage
#
FROM maven:3.8.6-eclipse-temurin-11-alpine AS build
#RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.32-r0/glibc-2.32-r0.apk && apk add glibc-2.32-r0.apk
RUN apk update && apk add gcompat
WORKDIR /app
COPY . .
RUN mvn dependency:go-offline -B
RUN mvn -Dmaven.test.skip=true clean package