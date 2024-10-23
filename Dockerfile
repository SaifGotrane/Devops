FROM openjdk:17-jdk-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
EXPOSE 8082
COPY target/devops-1.0.jar devops-1.0.jar
ENTRYPOINT ["java", "-jar", "/devops-1.0.jar"]
