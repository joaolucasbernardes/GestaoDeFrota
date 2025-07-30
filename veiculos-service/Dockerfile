FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn install -N
RUN mvn package -pl veiculos-service -am -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/veiculos-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]