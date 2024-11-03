FROM openjdk:17-jdk-alpine
EXPOSE 8082
COPY target/*.jar devops-1.0.jar
ENTRYPOINT ["java","-jar","/devops-1.0.jar"]