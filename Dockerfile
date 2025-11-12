FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

EXPOSE 8080

WORKDIR /app

COPY --from=build /app/target/PomoStudy-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]