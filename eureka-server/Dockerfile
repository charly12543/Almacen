FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/EurekaServer-0.0.1.jar
COPY ${JAR_FILE} app_EurekaServer.jar
ENTRYPOINT ["java", "-jar", "app_EurekaServer.jar"]
LABEL authors="Charly Bustamante"