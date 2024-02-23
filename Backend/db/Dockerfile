# Use the official MySQL 8.0 image from Docker Hub
FROM mysql:latest

# Environment variables for MySQL configuration
ENV MYSQL_DATABASE=harmonize-db \
    MYSQL_USER=harmonize \
    MYSQL_PASSWORD=harmonizepw \
    MYSQL_ROOT_PASSWORD=adminpw

# # Copy the init.sql script into the Docker container
# COPY init.sql /docker-entrypoint-initdb.d/

# Expose MySQL default port
EXPOSE 3306

# Commands to run when the container starts
CMD ["mysqld"]