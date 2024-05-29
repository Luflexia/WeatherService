FROM maven AS build

WORKDIR /app

#Свой айпи введи, его можно посмотреть через команду ifconfig
ENV DATABASE_URL 192.168.100.6:5432

COPY ./pom.xml ./

RUN mvn dependency:go-offline -DexcludeArtifactIds=maven-checkstyle-plugin

COPY ./src ./src

RUN mvn clean install -DskipTests -Pdocker

FROM openjdk:21-jdk-slim AS RUN

ENV DATABASE_URL db-container:5432
WORKDIR /app

COPY --from=build /app/target/*.jar /app/web.jar

EXPOSE 8080

CMD ["java", "-jar", "web.jar"]
