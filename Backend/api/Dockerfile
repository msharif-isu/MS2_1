FROM maven:latest
WORKDIR /home
COPY . .
RUN mvn clean package -DskipTests
