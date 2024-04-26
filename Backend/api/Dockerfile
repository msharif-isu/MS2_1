FROM maven:latest
WORKDIR /home
RUN mkdir -p /home/icons/
COPY . .
RUN mvn clean package -DskipTests