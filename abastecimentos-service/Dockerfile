FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

RUN mvn install -N
RUN mvn package -pl abastecimentos-service -am -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/abastecimentos-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]