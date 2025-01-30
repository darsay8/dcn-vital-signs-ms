FROM openjdk:23-slim
WORKDIR /app
COPY target/vital-signs-service-1.0-SNAPSHOT.jar app.jar
COPY wallet /app/wallet
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8082