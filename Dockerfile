FROM openjdk:17-jdk
EXPOSE 8082
ADD target/*.jar devops-1.0.jar
ENTRYPOINT ["java","-jar","/devops-1.0.jar"]