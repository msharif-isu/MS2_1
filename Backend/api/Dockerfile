FROM maven:latest
ENV DOCKER_RUNNING=true
WORKDIR /home
COPY . .
RUN mvn clean package -DskipTests
