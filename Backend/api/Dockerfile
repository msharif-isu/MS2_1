FROM maven:latest AS build-env
WORKDIR /home
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:latest AS run-env
WORKDIR /home
EXPOSE 8080
COPY --from=build-env /home .
ENTRYPOINT [ "java", "-jar", "/home/target/harmonize-api-1.0.0.jar" ]