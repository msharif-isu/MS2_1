FROM maven:latest AS build-env
WORKDIR /home
COPY . .
RUN mvn package


FROM openjdk:latest AS run-env
WORKDIR /home
EXPOSE 443
EXPOSE 80
COPY --from=build-env /home .
ENTRYPOINT [ "java", "-jar", "/home/target/hellopeople-1.0.0.jar" ]