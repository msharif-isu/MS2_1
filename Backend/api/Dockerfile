FROM maven:latest
ENV DOCKER_RUNNING=true
WORKDIR /home
RUN mkdir -p /home/icons/
COPY . .
RUN mvn clean package -DskipTests