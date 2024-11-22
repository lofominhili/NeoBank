ARG MAVEN_IMAGE=maven:3.9.4-eclipse-temurin-21
ARG JAVA_IMAGE=amazoncorretto:21-alpine-jdk

FROM ${MAVEN_IMAGE} AS builder
WORKDIR /app
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
COPY src src
RUN mvn clean package -DskipTests

FROM ${JAVA_IMAGE} AS layer_builder
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM ${JAVA_IMAGE} AS extractor
WORKDIR /app
COPY --from=layer_builder /app/dependencies/ ./
COPY --from=layer_builder /app/snapshot-dependencies/ ./
COPY --from=layer_builder /app/spring-boot-loader/ ./
COPY --from=layer_builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]