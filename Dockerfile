FROM openjdk:20-jdk-slim
ARG JAR_FILE=target/bazar-0.0.1.jar
COPY ${JAR_FILE} bazar.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","bazar.jar" ]