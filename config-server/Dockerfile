FROM eclipse-temurin:21-jre-alpine AS builder

WORKDIR /app

COPY target/*.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

EXPOSE 8888

ENTRYPOINT ["java", \
            "-Dspring.profiles.active=docker", \
            "org.springframework.boot.loader.launch.JarLauncher"]
