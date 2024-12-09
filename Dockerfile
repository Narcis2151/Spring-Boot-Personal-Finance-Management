# Start from the official MySQL image
FROM mysql:8.0

# Set environment variables:
ENV MYSQL_ROOT_PASSWORD=root_password
ENV MYSQL_DATABASE=personal_finance_db
ENV MYSQL_USER=spring_root
ENV MYSQL_PASSWORD=sprint_root_password

# Add the init_db.sql script to the /docker-entrypoint-initdb.d/ directory
COPY init_db.sql /docker-entrypoint-initdb.d/

# Expose the MySQL port
EXPOSE 3306
