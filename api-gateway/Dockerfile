FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/api-gateway-0.0.1.jar
COPY ${JAR_FILE} app_api-gateway.jar
ENTRYPOINT ["java", "-jar", "app_api-gateway.jar"]
LABEL authors="Charly Bustamante"