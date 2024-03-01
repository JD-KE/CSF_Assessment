FROM node:21 AS builder

ARG APP_DIR=/ngapp

WORKDIR ${APP_DIR}

RUN npm i -g @angular/cli

COPY /client/package* .
COPY /client/angular.json .
COPY /client/tsconfig.* .
COPY /client/src src

# RUN npm install
# Below uses package-lock.json, above will update packages to latest version
# npm ci is clean install, will install from package lock (module versions as during dev) instead of
# package.json (latest versions, may break the app)
RUN npm ci && ng build

# COPY /frontend/. .

# RUN ng build

FROM maven:3.9-eclipse-temurin-21 AS builder2

ARG APP_DIR1=/sbapp

WORKDIR ${APP_DIR1}

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

COPY --from=builder /ngapp/dist/client-side/browser/. ./src/main/resources/static/.

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk

ARG APP_DIR2=/JD
WORKDIR ${APP_DIR2}

ENV PORT=8080
ENV SPRING_DATA_MONGODB_URI=
ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=

COPY --from=builder2 /sbapp/target/ecommerce-0.0.1-SNAPSHOT.jar csf_assessment.jar

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar csf_assessment.jar