# Stage 1: Build the application using Maven and JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the WAR file skipping unit tests
RUN mvn clean package -DskipTests

# Stage 2: Run application on Apache Tomcat 10.1 (Jakarta EE 10/11 compatible)
FROM tomcat:10.1-jdk17

# Remove default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy our WAR file as the root application (ROOT.war)
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
