FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/carrito-compras-0.0.1.jar
COPY ${JAR_FILE} app_carrito-compras.jar
ENTRYPOINT ["java", "-jar", "app_carrito-compras.jar"]
LABEL authors="Charly Bustamante"
