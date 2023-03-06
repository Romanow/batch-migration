FROM amazoncorretto:17 as builder
WORKDIR application
COPY build/libs/migration-service.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:17
WORKDIR application
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]